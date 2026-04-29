package com.example.fhchatroom.data

data class Room(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val members: List<String> = emptyList(),
    val ownerEmail: String = "",
    val lastMessage: String = "",
    val lastMessageSender: String = "",
    val lastMessageTimestamp: Long = 0L,
    val lastMessageType: String = "TEXT",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = 0L,
    val isPrivate: Boolean = false,
    val isDirect: Boolean = false,
    val hiddenBy: List<String> = emptyList(), // New field to track users who have hidden this room
    val templateRoom: Boolean = false,
    val academicStudyPath: String = "",
    val academicSemester: Long = 0L,
    val academicRoomKind: String = "",
    val lectureName: String = ""
)
