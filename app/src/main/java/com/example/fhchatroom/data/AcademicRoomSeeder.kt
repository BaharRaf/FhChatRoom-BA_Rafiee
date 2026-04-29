package com.example.fhchatroom.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class AcademicRoomSeeder(
    private val firestore: FirebaseFirestore
) {
    suspend fun ensureRoomsForUser(user: User) {
        syncRoomsForUser(user)
    }

    suspend fun syncRoomsForUser(user: User) {
        val email = user.email.trim()
        if (email.isBlank()) {
            return
        }

        val templates = academicRoomTemplatesFor(user.studyPath, user.semester)
        runCatching {
            removeStaleAcademicMemberships(
                studentEmail = email,
                currentAcademicRoomIds = templates.map { it.id }.toSet()
            )
        }

        if (templates.isEmpty()) {
            return
        }

        for (template in templates) {
            ensureRoom(template, email)
        }
    }

    private suspend fun removeStaleAcademicMemberships(
        studentEmail: String,
        currentAcademicRoomIds: Set<String>
    ) {
        val snapshot = firestore.collection("rooms")
            .whereArrayContains("members", studentEmail)
            .get()
            .await()
        val now = System.currentTimeMillis()

        for (document in snapshot.documents) {
            val isTemplateRoom = document.getBoolean("templateRoom") ?: false
            if (!isTemplateRoom || document.id in currentAcademicRoomIds) {
                continue
            }

            document.reference.update(
                mapOf(
                    "members" to FieldValue.arrayRemove(studentEmail),
                    "updatedAt" to now
                )
            ).await()
        }
    }

    private suspend fun ensureRoom(template: AcademicRoomTemplate, studentEmail: String) {
        val roomRef = firestore.collection("rooms").document(template.id)
        val now = System.currentTimeMillis()

        // Use a deterministic merge so predefined rooms can be queued offline and safely re-synced.
        // Do not overwrite lastMessage fields here, otherwise syncing templates would erase chat previews.
        roomRef.set(
            mapOf(
                "name" to template.name,
                "description" to template.description,
                "category" to template.category,
                "members" to FieldValue.arrayUnion(studentEmail),
                "ownerEmail" to "system",
                "updatedAt" to now,
                "isPrivate" to false,
                "private" to false,
                "isDirect" to false,
                "direct" to false,
                "academicStudyPath" to template.studyPath,
                "academicSemester" to template.semester,
                "academicRoomKind" to template.kind,
                "lectureName" to template.lectureName,
                "templateRoom" to true
            )
        ).await()
    }
}
