package com.example.fhchatroom.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fhchatroom.Injection
import com.example.fhchatroom.data.AcademicRoomSeeder
import com.example.fhchatroom.data.Room
import com.example.fhchatroom.data.User
import com.example.fhchatroom.data.toRoomOrNull
import com.example.fhchatroom.data.toUserOrNull
import com.example.fhchatroom.data.withRepairedAcademicProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RoomViewModel : ViewModel() {
    companion object {
        private const val TAG = "RoomViewModel"
    }

    private val _rooms = MutableLiveData<List<Room>>()
    val rooms: LiveData<List<Room>> get() = _rooms
    private val _currentAcademicUser = MutableLiveData<User?>(null)
    val currentAcademicUser: LiveData<User?> get() = _currentAcademicUser
    private val _academicRoomSyncError = MutableLiveData<String?>(null)
    val academicRoomSyncError: LiveData<String?> get() = _academicRoomSyncError

    private val firestore = Injection.instance()
    private var roomListener: ListenerRegistration? = null
    private val auth = FirebaseAuth.getInstance()
    private val academicRoomSeeder = AcademicRoomSeeder(firestore)
    private val authStateListener = FirebaseAuth.AuthStateListener {
        syncAcademicRoomsForCurrentUser()
    }

    init {
        observeRoomsInRealTime()
        auth.addAuthStateListener(authStateListener)
        syncAcademicRoomsForCurrentUser()
    }

    private fun observeRoomsInRealTime() {
        roomListener = firestore.collection("rooms")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                val self = auth.currentUser?.email
                if (snapshot != null) {
                    var updatedRooms = snapshot.documents.mapNotNull { doc ->
                        val room = doc.toRoomOrNull() ?: return@mapNotNull null

                        // Don't show room if user has hidden it
                        if (self != null && room.hiddenBy.contains(self)) {
                            return@mapNotNull null
                        }

                        room
                    }

                    // Visibility: if signed-in, keep public + your private rooms; otherwise keep only public
                    updatedRooms = if (self != null) {
                        updatedRooms.filter { !it.isPrivate || it.ownerEmail == self || it.members.contains(self) }
                    } else {
                        updatedRooms.filter { !it.isPrivate }
                    }

                    _rooms.value = updatedRooms

                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        roomListener?.remove()
        auth.removeAuthStateListener(authStateListener)
    }

    fun syncAcademicRoomsForCurrentUser() {
        viewModelScope.launch {
            val email = auth.currentUser?.email?.trim() ?: return@launch
            runCatching {
                val rawUser = loadCurrentUserForAcademicSync(email) ?: return@launch
                val user = rawUser.withRepairedAcademicProfile()
                _currentAcademicUser.value = user

                if (user != rawUser) {
                    runCatching {
                        persistAcademicProfileRepair(user)
                    }.onFailure { repairError ->
                        Log.w(TAG, "Failed to repair academic profile for $email", repairError)
                    }
                }

                academicRoomSeeder.syncRoomsForUser(user)
                _academicRoomSyncError.value = null
            }.onFailure { error ->
                if (error is CancellationException) {
                    return@onFailure
                }
                Log.w(TAG, "Failed to sync predefined academic rooms for $email", error)
                _academicRoomSyncError.value = error.message ?: error::class.java.simpleName
            }
        }
    }

    fun clearAcademicRoomSyncError() {
        _academicRoomSyncError.value = null
    }

    private suspend fun loadCurrentUserForAcademicSync(email: String) =
        runCatching {
            firestore.collection("users")
                .document(email)
                .get()
                .await()
        }.getOrElse {
            firestore.collection("users")
                .document(email)
                .get(Source.CACHE)
                .await()
        }.toUserOrNull()

    private suspend fun persistAcademicProfileRepair(user: User) {
        firestore.collection("users")
            .document(user.email)
            .set(
                mapOf(
                    "studyPath" to user.studyPath,
                    "semester" to user.semester,
                    "semesterBucket" to user.semesterBucket
                ),
                SetOptions.merge()
            )
            .await()
    }

    fun createRoom(name: String, description: String, category: String = "") =
        createRoom(name, description, category, isPrivate = false)

    fun createRoom(name: String, description: String, category: String, isPrivate: Boolean) {
        val email = FirebaseAuth.getInstance().currentUser?.email
        val newRoom = Room(
            name = name.trim(),
            description = description.trim(),
            category = category.trim(),
            members = email?.let { listOf(it) } ?: emptyList(),
            ownerEmail = email ?: "",
            isPrivate = isPrivate,
            isDirect = false,
            createdAt = System.currentTimeMillis()
        )
        // Write the room, then add compatibility fields ("private")
        firestore.collection("rooms").add(newRoom)
            .addOnSuccessListener { ref ->
                // Keep both for compatibility with existing docs/readers
                ref.update(mapOf("private" to isPrivate))
            }
    }

    fun inviteToRoom(roomId: String, email: String, onResult: (Boolean, String) -> Unit) {
        // First check if the room exists and get its details
        firestore.collection("rooms").document(roomId)
            .get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    onResult(false, "Room not found")
                    return@addOnSuccessListener
                }

                val room = document.toRoomOrNull()
                val currentUserEmail = auth.currentUser?.email

                // Check if current user is owner or member (for any type of group)
                // Allow inviting to any room where the user is a member, except DMs
                if (room?.isDirect == true) {
                    onResult(false, "Cannot invite to direct messages")
                    return@addOnSuccessListener
                }

                // Check if user has permission to invite (must be owner or member)
                if (room?.ownerEmail != currentUserEmail && room?.members?.contains(currentUserEmail) != true) {
                    onResult(false, "You don't have permission to invite to this room")
                    return@addOnSuccessListener
                }

                // Check if user is already a member
                if (room?.members?.contains(email) == true) {
                    onResult(false, "User is already a member")
                    return@addOnSuccessListener
                }

                // Add the user to the room
                firestore.collection("rooms").document(roomId)
                    .update("members", FieldValue.arrayUnion(email))
                    .addOnSuccessListener {
                        onResult(true, "User invited successfully")
                    }
                    .addOnFailureListener {
                        onResult(false, "Failed to invite user")
                    }
            }
            .addOnFailureListener {
                onResult(false, "Failed to check room details")
            }
    }

    fun joinRoom(roomId: String) {
        val email = FirebaseAuth.getInstance().currentUser?.email ?: return
        firestore.collection("rooms")
            .document(roomId)
            .update("members", FieldValue.arrayUnion(email))
    }

    fun leaveRoom(roomId: String, onComplete: (Boolean) -> Unit = {}) {
        val email = FirebaseAuth.getInstance().currentUser?.email ?: return

        firestore.collection("rooms").document(roomId)
            .get()
            .addOnSuccessListener { document ->
                val room = document.toRoomOrNull()

                if (room?.isDirect == true) {
                    // For DMs, add to hiddenBy list instead of removing from members
                    firestore.collection("rooms").document(roomId)
                        .update("hiddenBy", FieldValue.arrayUnion(email))
                        .addOnSuccessListener { onComplete(true) }
                        .addOnFailureListener { onComplete(false) }
                } else {
                    // For regular rooms, remove from members
                    firestore.collection("rooms").document(roomId)
                        .update("members", FieldValue.arrayRemove(email))
                        .addOnSuccessListener { onComplete(true) }
                        .addOnFailureListener { onComplete(false) }
                }
            }
            .addOnFailureListener { onComplete(false) }
    }

    fun hideDM(roomId: String, onComplete: (Boolean) -> Unit = {}) {
        val email = FirebaseAuth.getInstance().currentUser?.email ?: return

        // Add current user to hiddenBy array
        firestore.collection("rooms").document(roomId)
            .update("hiddenBy", FieldValue.arrayUnion(email))
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun unhideDM(roomId: String, onComplete: (Boolean) -> Unit = {}) {
        val email = FirebaseAuth.getInstance().currentUser?.email ?: return

        // Remove current user from hiddenBy array
        firestore.collection("rooms").document(roomId)
            .update("hiddenBy", FieldValue.arrayRemove(email))
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun deleteRoom(roomId: String) {
        firestore.collection("rooms").document(roomId).delete()
    }

    /**
     * Open or create a 1:1 DM room between the current user and targetEmail.
     */
    fun openOrCreateDirectRoom(targetEmail: String, onResult: (String) -> Unit) {
        val self = FirebaseAuth.getInstance().currentUser?.email ?: return
        val roomsRef = firestore.collection("rooms")

        // 1) Find ANY 2-member room containing both users (regardless of isDirect).
        roomsRef
            .whereArrayContains("members", self)
            .get()
            .addOnSuccessListener { qs ->
                val existing = qs.documents.firstOrNull { doc ->
                    val members = (doc.get("members") as? List<*>)
                        ?.mapNotNull { it as? String }
                        ?: emptyList()
                    members.size == 2 && members.contains(targetEmail)
                }

                if (existing != null) {
                    val isDirect = (existing.get("isDirect") as? Boolean)
                        ?: (existing.get("direct") as? Boolean) ?: false
                    val hiddenBy = (existing.get("hiddenBy") as? List<*>)
                        ?.mapNotNull { it as? String }
                        ?: emptyList()

                    // If user had hidden this DM, unhide it
                    if (hiddenBy.contains(self)) {
                        existing.reference.update("hiddenBy", FieldValue.arrayRemove(self))
                    }

                    if (!isDirect) {
                        // Normalize legacy 2-member room to a DM (set both field name variants)
                        existing.reference.update(
                            mapOf(
                                "isDirect" to true,
                                "isPrivate" to true,
                                "direct" to true,
                                "private" to true
                            )
                        ).addOnCompleteListener {
                            onResult(existing.id)
                        }
                    } else {
                        onResult(existing.id)
                    }
                } else {
                    // 2) Create a brand new DM
                    val dm = Room(
                        name = "DM",
                        description = "",
                        members = listOf(self, targetEmail),
                        ownerEmail = self,
                        isPrivate = true,
                        isDirect = true,
                        createdAt = System.currentTimeMillis()
                    )
                    roomsRef.add(dm).addOnSuccessListener { ref ->
                        // Also set compatibility keys
                        ref.update(mapOf("private" to true, "direct" to true))
                        onResult(ref.id)
                    }
                }
            }
    }
}
