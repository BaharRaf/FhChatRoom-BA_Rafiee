package com.example.fhchatroom.data

import com.google.firebase.firestore.DocumentSnapshot

private fun coerceLong(value: Any?, default: Long = 0L): Long {
    return when (value) {
        is Long -> value
        is Int -> value.toLong()
        is Double -> value.toLong()
        is Float -> value.toLong()
        is Number -> value.toLong()
        is String -> value.toLongOrNull() ?: default
        else -> default
    }
}

private fun coerceBoolean(value: Any?, default: Boolean = false): Boolean {
    return when (value) {
        is Boolean -> value
        is Number -> value.toInt() != 0
        is String -> value.equals("true", ignoreCase = true) || value == "1"
        else -> default
    }
}

private fun stringList(value: Any?): List<String> {
    return (value as? List<*>)
        ?.mapNotNull { it as? String }
        ?: emptyList()
}

fun DocumentSnapshot.toRoomOrNull(): Room? {
    val name = (getString("name") ?: "").trim()
    val members = stringList(get("members"))
    val ownerEmail = (getString("ownerEmail") ?: "").trim()

    if (name.isBlank() && members.isEmpty() && ownerEmail.isBlank()) {
        return null
    }

    return Room(
        id = id,
        name = name,
        description = getString("description") ?: "",
        category = getString("category") ?: "",
        members = members,
        ownerEmail = ownerEmail,
        lastMessage = getString("lastMessage") ?: "",
        lastMessageSender = getString("lastMessageSender") ?: "",
        lastMessageTimestamp = coerceLong(get("lastMessageTimestamp")),
        lastMessageType = getString("lastMessageType") ?: "TEXT",
        createdAt = coerceLong(get("createdAt"), System.currentTimeMillis()),
        updatedAt = coerceLong(get("updatedAt")),
        isPrivate = coerceBoolean(get("isPrivate"), coerceBoolean(get("private"))),
        isDirect = coerceBoolean(get("isDirect"), coerceBoolean(get("direct"))),
        hiddenBy = stringList(get("hiddenBy")),
        templateRoom = coerceBoolean(get("templateRoom")),
        academicStudyPath = getString("academicStudyPath") ?: "",
        academicSemester = coerceLong(get("academicSemester")),
        academicRoomKind = getString("academicRoomKind") ?: "",
        lectureName = getString("lectureName") ?: ""
    )
}
