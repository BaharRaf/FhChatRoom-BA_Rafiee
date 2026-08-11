package com.example.fhchatroom.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fhchatroom.Injection
import com.example.fhchatroom.data.Room
import com.example.fhchatroom.data.User
import com.example.fhchatroom.data.studyPathCatalogKey
import com.example.fhchatroom.data.toUserOrNull
import com.example.fhchatroom.viewmodel.RecommendationViewModel
import com.example.fhchatroom.viewmodel.RoomViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext

/**
 * Resolves DM partner display names at most once per process. Without this,
 * every DM row in the room list issued its own Firestore user query on each
 * (re)composition -- an N+1 lookup pattern. Failures are not cached so a
 * transient error is retried the next time the row appears.
 */
private object DmTitleCache {
    private val cache = java.util.concurrent.ConcurrentHashMap<String, String>()

    suspend fun displayNameFor(email: String): String? {
        cache[email]?.let { return it.ifEmpty { null } }
        return try {
            val qs = Injection.instance()
                .collection("users")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .await()
            val user = qs.documents.firstOrNull()?.toUserOrNull()
            val full = listOfNotNull(user?.firstName?.trim(), user?.lastName?.trim())
                .joinToString(" ")
                .trim()
            cache[email] = full
            full.ifEmpty { null }
        } catch (_: Exception) {
            null
        }
    }
}

enum class SortOption {
    NAME_ASC,
    NAME_DESC,
    MEMBER_COUNT_ASC,
    MEMBER_COUNT_DESC,
    NEWEST_FIRST,
    OLDEST_FIRST
}
private enum class RoomTab { PUBLIC, PRIVATE }
private enum class PublicRoomFilter { ALL, ACADEMIC, STUDENT }

@Composable
fun ChatRoomListScreen(
    roomViewModel: RoomViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    recommendationViewModel: RecommendationViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onJoinClicked: (Room) -> Unit,
    onLogout: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToFriends: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val rooms by roomViewModel.rooms.observeAsState(emptyList())
    val recommendationState by recommendationViewModel.uiState.observeAsState()
    val currentAcademicUser by roomViewModel.currentAcademicUser.observeAsState()
    val academicRoomSyncError by roomViewModel.academicRoomSyncError.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Search and Sort states
    var searchQuery by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf(SortOption.NAME_ASC) }
    var showSortMenu by remember { mutableStateOf(false) }

    var tab by remember { mutableStateOf(RoomTab.PUBLIC) }
    var publicRoomFilter by remember { mutableStateOf(PublicRoomFilter.ALL) }

    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
    val context = LocalContext.current

    LaunchedEffect(currentUserEmail) {
        roomViewModel.syncAcademicRoomsForCurrentUser()
    }

    LaunchedEffect(academicRoomSyncError) {
        academicRoomSyncError?.let { error ->
            Toast.makeText(context, "Predefined room sync failed: $error", Toast.LENGTH_LONG).show()
            roomViewModel.clearAcademicRoomSyncError()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Top app bar
        ChatAppTopBar(
            onLogout = onLogout,
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToFriends = onNavigateToFriends,
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme
        )

        // Public/Private toggle
        Spacer(modifier = Modifier.height(6.dp))
        SegmentedButtons(tab) { tab = it }

        Spacer(modifier = Modifier.height(8.dp))

        if (tab == RoomTab.PUBLIC) {
            PublicRoomFilterButtons(publicRoomFilter) { publicRoomFilter = it }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Header (no counts)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chat Rooms",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )

            Row {
                // Search toggle
                IconButton(onClick = {
                    showSearchBar = !showSearchBar
                    if (!showSearchBar) searchQuery = ""
                }) {
                    Icon(
                        imageVector = if (showSearchBar) Icons.Filled.Clear else Icons.Filled.Search,
                        contentDescription = if (showSearchBar) "Close Search" else "Search Rooms"
                    )
                }

                // Sort
                IconButton(onClick = { showSortMenu = true }) {
                    Icon(
                        imageVector = Icons.Filled.Sort,
                        contentDescription = "Sort Rooms"
                    )
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    DropdownMenuItem(text = { Text("Name (A-Z) ↑") }, onClick = { sortOption = SortOption.NAME_ASC; showSortMenu = false })
                    DropdownMenuItem(text = { Text("Name (Z-A) ↓") }, onClick = { sortOption = SortOption.NAME_DESC; showSortMenu = false })
                    DropdownMenuItem(text = { Text("Members (Low to High) ↑") }, onClick = { sortOption = SortOption.MEMBER_COUNT_ASC; showSortMenu = false })
                    DropdownMenuItem(text = { Text("Members (High to Low) ↓") }, onClick = { sortOption = SortOption.MEMBER_COUNT_DESC; showSortMenu = false })
                    DropdownMenuItem(text = { Text("Newest First") }, onClick = { sortOption = SortOption.NEWEST_FIRST; showSortMenu = false })
                    DropdownMenuItem(text = { Text("Oldest First") }, onClick = { sortOption = SortOption.OLDEST_FIRST; showSortMenu = false })
                }
            }
        }

        // Animated Search Bar
        AnimatedVisibility(
            visible = showSearchBar,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search rooms by name or description...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true
            )
        }

        // TAB FILTERING
        // PUBLIC: predefined academic rooms plus student-created public rooms.
        // PRIVATE: private user-created rooms and DMs.
        val tabRooms = when (tab) {
            RoomTab.PUBLIC -> {
                val academicRooms = rooms.filter { room ->
                    room.matchesAcademicProfile(currentAcademicUser)
                }
                val studentPublicRooms = rooms.filter { room ->
                    !room.isPrivate && !room.isDirect && !room.isAcademicRoom()
                }

                when (publicRoomFilter) {
                    PublicRoomFilter.ALL -> academicRooms + studentPublicRooms
                    PublicRoomFilter.ACADEMIC -> academicRooms
                    PublicRoomFilter.STUDENT -> studentPublicRooms
                }
            }
            RoomTab.PRIVATE -> rooms.filter { room ->
                room.isPrivate &&
                        !room.isAcademicRoom() &&
                        currentUserEmail != null &&
                        (room.members.contains(currentUserEmail) || room.ownerEmail == currentUserEmail)
            }
        }

        // Apply search on the tab list
        val filteredRooms = tabRooms.filter { room ->
            searchQuery.isEmpty() ||
                    room.name.contains(searchQuery, ignoreCase = true) ||
                    room.description.contains(searchQuery, ignoreCase = true)
        }

        // Sort after filtering
        val sortedRooms = when (sortOption) {
            SortOption.NAME_ASC -> filteredRooms.sortedBy { it.name.lowercase() }
            SortOption.NAME_DESC -> filteredRooms.sortedByDescending { it.name.lowercase() }
            SortOption.MEMBER_COUNT_ASC -> filteredRooms.sortedBy { it.members.size }
            SortOption.MEMBER_COUNT_DESC -> filteredRooms.sortedByDescending { it.members.size }
            SortOption.NEWEST_FIRST -> filteredRooms.sortedByDescending { it.lastMessageTimestamp }
            SortOption.OLDEST_FIRST -> filteredRooms.sortedBy { it.lastMessageTimestamp }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // AI recommendations (BA2): groups suggested by the offline GraphSAGE
        // pipeline, surfaced from users/{email}.recommendedRoomIds.
        val recommendedRooms = if (tab == RoomTab.PUBLIC && searchQuery.isEmpty()) {
            val recommendedIds = recommendationState?.roomIds.orEmpty()
            recommendedIds.mapNotNull { roomId ->
                rooms.firstOrNull { room ->
                    room.id == roomId &&
                            !room.isDirect &&
                            !room.isPrivate &&
                            // Recommend only student-made groups; academic
                            // template rooms are auto-assigned scaffolding, not
                            // discoveries (defense-in-depth with the pipeline).
                            !room.templateRoom &&
                            room.academicRoomKind.isBlank() &&
                            currentUserEmail != null &&
                            !room.members.contains(currentUserEmail)
                }
            }.take(5)
        } else {
            emptyList()
        }

        if (recommendedRooms.isNotEmpty()) {
            RecommendedRoomsSection(
                rooms = recommendedRooms,
                source = recommendationState?.source ?: "NONE",
                onJoinClicked = { room ->
                    roomViewModel.joinRoom(room.id)
                    onJoinClicked(room)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // List
        val currentUser = currentUserEmail
        if (sortedRooms.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = when {
                        searchQuery.isNotEmpty() -> "No rooms found matching \"$searchQuery\""
                        academicRoomSyncError != null -> "Could not sync predefined rooms. Check Logcat for the Firestore error."
                        tab == RoomTab.PUBLIC && publicRoomFilter == PublicRoomFilter.ACADEMIC -> "No academic rooms available. Check your study path and semester in Profile."
                        tab == RoomTab.PUBLIC && publicRoomFilter == PublicRoomFilter.STUDENT -> "No student-made public rooms yet."
                        tab == RoomTab.PUBLIC -> "No public rooms available yet."
                        else -> "No private rooms available"
                    },
                    color = Color.Gray
                )
                if (searchQuery.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = { searchQuery = "" }) {
                        Text("Clear search")
                    }
                }
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(sortedRooms, key = { it.id }) { room ->
                    val isMember = currentUser != null && room.members.contains(currentUser)
                    val isOwner = currentUser != null && (
                            room.ownerEmail == currentUser ||
                                    (room.ownerEmail.isBlank() && room.members.firstOrNull() == currentUser)
                            )

                    RoomItem(
                        room = room,
                        isMember = isMember,
                        isOwner = isOwner,
                        memberCount = room.members.size,
                        onDeleteClicked = {
                            // Only allow delete for non-DM groups
                            if (!room.isDirect) {
                                roomViewModel.deleteRoom(room.id)
                            }
                        },
                        onJoinClicked = {
                            if (!isMember) {
                                roomViewModel.joinRoom(room.id)
                            }
                            onJoinClicked(room)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (tab == RoomTab.PRIVATE) "Create Private Room" else "Create Public Room")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(if (tab == RoomTab.PRIVATE) "Create a private room" else "Create a public room")
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Room Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            maxLines = 3
                        )
                        Text(
                            text = if (tab == RoomTab.PRIVATE) {
                                "Private rooms are visible only to invited members."
                            } else {
                                "Public student-made rooms are visible to all students. Academic rooms are still created automatically."
                            },
                            modifier = Modifier.padding(8.dp),
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {
                            if (name.isNotBlank()) {
                                roomViewModel.createRoom(
                                    name = name,
                                    description = description,
                                    category = if (tab == RoomTab.PRIVATE) "" else "Student",
                                    isPrivate = tab == RoomTab.PRIVATE
                                )
                                if (tab == RoomTab.PUBLIC) {
                                    publicRoomFilter = PublicRoomFilter.STUDENT
                                }
                                name = ""
                                description = ""
                                showDialog = false
                            }
                        }) { Text("Add") }

                        Button(onClick = {
                            showDialog = false
                            name = ""
                            description = ""
                        }) { Text("Cancel") }
                    }
                }
            )
        }
    }
}

@Composable
private fun RecommendedRoomsSection(
    rooms: List<Room>,
    source: String,
    onJoinClicked: (Room) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recommended for you",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = when (source) {
                    "GRAPH_SAGE_LOCAL" -> "GraphSAGE"
                    "LIGHT_GCN_LOCAL" -> "LightGCN"
                    "CONTENT_BASED" -> "Content-based"
                    else -> ""
                },
                fontSize = 11.sp,
                color = Color.Gray
            )
        }

        rooms.forEach { room ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = room.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (room.description.isNotEmpty()) {
                            Text(
                                text = room.description,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        Text(
                            text = "${room.members.size} ${if (room.members.size == 1) "member" else "members"}",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    OutlinedButton(onClick = { onJoinClicked(room) }) {
                        Text("Join")
                    }
                }
            }
        }
    }
}

@Composable
private fun SegmentedButtons(current: RoomTab, onChange: (RoomTab) -> Unit) {
    Row(
        Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            .padding(4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        // Label kept as "Private"
        listOf(RoomTab.PUBLIC to "Public", RoomTab.PRIVATE to "Private").forEach { (value, label) ->
            val selected = current == value
            val bg = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
            val fg = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            Text(
                label,
                color = fg,
                modifier = Modifier
                    .background(bg, CircleShape)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clickable { onChange(value) },
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun PublicRoomFilterButtons(
    current: PublicRoomFilter,
    onChange: (PublicRoomFilter) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(
            PublicRoomFilter.ALL to "All",
            PublicRoomFilter.ACADEMIC to "Academic",
            PublicRoomFilter.STUDENT to "Student-made"
        ).forEach { (value, label) ->
            val selected = current == value
            Text(
                text = label,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .background(
                        color = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        },
                        shape = CircleShape
                    )
                    .clickable { onChange(value) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun RoomItem(
    room: Room,
    isMember: Boolean = false,
    isOwner: Boolean = false,
    memberCount: Int = 0,
    onDeleteClicked: (Room) -> Unit = {},
    onHideClicked: (Room) -> Unit = {},
    onJoinClicked: (Room) -> Unit,
    roomViewModel: RoomViewModel = viewModel()
) {
    val context = LocalContext.current
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val isAcademicRoom = room.isAcademicRoom()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isMember)
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val currentEmail = FirebaseAuth.getInstance().currentUser?.email
                val dmOther = room.members.firstOrNull { it != currentEmail } ?: room.members.firstOrNull()

                // Title: for DMs, show other user's name (fallback to email), else room.name
                var titleText by remember { mutableStateOf(room.name) }
                LaunchedEffect(room.id, dmOther, room.isDirect) {
                    titleText = if (room.isDirect && dmOther != null) {
                        DmTitleCache.displayNameFor(dmOther) ?: dmOther
                    } else {
                        room.name
                    }
                }

                Text(
                    text = titleText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                RoomTags(
                    room = room,
                    isMember = isMember,
                    isAcademicRoom = isAcademicRoom
                )

                // Last message preview / description
                if (room.lastMessage.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${room.lastMessageSender}: ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = room.lastMessage,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, false)
                        )
                        if (room.lastMessageTimestamp > 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = formatMessageTime(room.lastMessageTimestamp),
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else if (room.description.isNotEmpty() && !room.isDirect) {
                    Text(
                        text = room.description,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }

                if (!room.isDirect) {
                    Text(
                        text = "$memberCount ${if (memberCount == 1) "member" else "members"}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                } else {
                    Text(
                        text = "Direct Message",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(onClick = { onJoinClicked(room) }) {
                    Text(if (isMember) "Enter" else "Join")
                }

                // Show delete/hide button based on room type
                if (room.isDirect) {
                    // For DMs, show hide button
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { showDeleteConfirmation = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "Hide Chat",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                } else if (isOwner && !room.isDirect) {
                    // For regular rooms, show delete for owner
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { onDeleteClicked(room) }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete Room")
                    }
                }
            }
        }
    }

    // Confirmation dialog for hiding DMs
    if (showDeleteConfirmation && room.isDirect) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Hide Conversation") },
            text = {
                Text("This will hide the conversation from your chat list. You can still access it by messaging this person again. The other person will still see the conversation.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        roomViewModel.hideDM(room.id) { success ->
                            if (success) {
                                Toast.makeText(context, "Conversation hidden", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to hide conversation", Toast.LENGTH_SHORT).show()
                            }
                        }
                        showDeleteConfirmation = false
                    }
                ) {
                    Text("Hide")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun RoomTags(
    room: Room,
    isMember: Boolean,
    isAcademicRoom: Boolean
) {
    val labels = buildList {
        when {
            room.isDirect -> add("DM")
            isAcademicRoom -> add(
                if (room.category.equals("Lecture", ignoreCase = true)) "Lecture" else "Main"
            )
            !room.isPrivate -> add("Student")
            room.isPrivate -> add("Private")
        }

        if (isMember && !room.isDirect && !isAcademicRoom) {
            add("Joined")
        }
    }

    if (labels.isEmpty()) {
        return
    }

    Row(
        modifier = Modifier.padding(top = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        labels.forEach { label ->
            Text(
                text = label,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        shape = CircleShape
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

// Helper function to format timestamp for last message
private fun formatMessageTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60_000 -> "now"
        diff < 3_600_000 -> "${diff / 60_000}m"
        diff < 86_400_000 -> "${diff / 3_600_000}h"
        diff < 604_800_000 -> "${diff / 86_400_000}d"
        else -> {
            val date = java.util.Date(timestamp)
            val format = java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault())
            format.format(date)
        }
    }
}

private fun Room.isAcademicRoom(): Boolean {
    return templateRoom ||
            category.equals("Academic", ignoreCase = true) ||
            category.equals("Lecture", ignoreCase = true)
}

private fun Room.matchesAcademicProfile(user: User?): Boolean {
    val studyPath = user?.studyPath?.trim().orEmpty()
    val semester = user?.semester ?: 0L

    if (!isAcademicRoom() || studyPath.isBlank() || semester <= 0) return false
    if (studyPathCatalogKey(academicStudyPath) != studyPathCatalogKey(studyPath)) return false

    // Exactly two academic rooms are shown per student:
    //   STUDY_PATH -> the whole-degree room, shared across every semester
    //                 (academicSemester == 0, so it must not be gated by semester), and
    //   SEMESTER   -> the room for the student's own semester.
    // Legacy lecture rooms from older app versions are no longer surfaced;
    // unknown/blank kinds fall back to the original semester-match behaviour so
    // no legitimately-kinded room regresses.
    return when (academicRoomKind) {
        "STUDY_PATH" -> true
        "SEMESTER" -> academicSemester == semester
        "LECTURE" -> false
        else -> academicSemester == semester
    }
}
