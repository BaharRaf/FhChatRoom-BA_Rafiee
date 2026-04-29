package com.example.fhchatroom.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val TAG = "UserRepository"
    }

    private val academicRoomSeeder = AcademicRoomSeeder(firestore)

    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        studyPath: String,
        semester: Long
    ): Result<Boolean> = try {
        auth.createUserWithEmailAndPassword(email, password).await()
        val repairedAcademicProfile = repairAcademicProfile(studyPath, semester)
        val user = User(
            firstName = firstName,
            lastName = lastName,
            email = email,
            studyPath = repairedAcademicProfile.studyPath,
            semester = repairedAcademicProfile.semester,
            semesterBucket = repairedAcademicProfile.semesterBucket,
            isOnline = true
        )
        saveUserToFirestore(user)
        syncAcademicRoomsBestEffort(user)
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }


    suspend fun login(email: String, password: String): Result<Boolean> = try {
        auth.signInWithEmailAndPassword(email, password).await()
        ensureUserDocumentExists(auth.currentUser?.email ?: email.trim())?.let { user ->
            syncAcademicRoomsBestEffort(user)
        }
        Result.Success(true)
    } catch(e: Exception) {
        Result.Error(e)
    }

    private suspend fun ensureUserDocumentExists(email: String): User? {
        val normalizedEmail = email.trim()
        if (normalizedEmail.isBlank()) return null

        val document = firestore.collection("users")
            .document(normalizedEmail)
            .get()
            .await()

        if (document.exists()) {
            val existingUser = document.toUserOrNull() ?: return null
            return repairAcademicProfileIfNeeded(existingUser)
        }

        val fallbackUser = buildFallbackUser(normalizedEmail)
        firestore.collection("users")
            .document(normalizedEmail)
            .set(fallbackUser, SetOptions.merge())
            .await()
        return fallbackUser
    }

    private fun buildFallbackUser(email: String): User {
        val localPart = email.substringBefore("@").trim()
        val readableLocalPart = localPart
            .replace(".", " ")
            .replace("_", " ")
            .replace("-", " ")
            .trim()

        val fallbackFirstName = readableLocalPart
            .split("\\s+".toRegex())
            .firstOrNull()
            ?.replaceFirstChar { it.uppercase() }
            .orEmpty()

        return User(
            firstName = fallbackFirstName,
            lastName = "",
            email = email,
            studyPath = "",
            semester = 0L,
            semesterBucket = semesterBucketFor(0L),
            isOnline = false
        )
    }

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users")
            .document(user.email)
            .set(user)
            .await()
    }

    private suspend fun repairAcademicProfileIfNeeded(user: User): User {
        val repairedUser = user.withRepairedAcademicProfile()
        if (repairedUser == user) {
            return user
        }

        firestore.collection("users")
            .document(user.email)
            .set(
                mapOf(
                    "studyPath" to repairedUser.studyPath,
                    "semester" to repairedUser.semester,
                    "semesterBucket" to repairedUser.semesterBucket
                ),
                SetOptions.merge()
            )
            .await()

        return repairedUser
    }

    private suspend fun syncAcademicRoomsBestEffort(user: User) {
        runCatching {
            academicRoomSeeder.syncRoomsForUser(user)
        }.onFailure { error ->
            Log.w(TAG, "Failed to sync academic rooms for ${user.email}", error)
        }
    }


    suspend fun getCurrentUser(): Result<User> = try {
        val uid = auth.currentUser?.email
        if (uid != null) {
            val userDocument = firestore.collection("users")
                .document(uid)
                .get()
                .await()
            val user = userDocument.toUserOrNull()?.let { repairAcademicProfileIfNeeded(it) }
            if (user != null) {
                Result.Success(user)
            } else {
                Result.Error(Exception("User data not found"))
            }
        } else {
            Result.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }
    suspend fun getUserByEmail(email: String): Result<User> = try {
        val userDocument = firestore.collection("users")
            .document(email)
            .get()
            .await()
        val user = userDocument.toUserOrNull()?.let { repairAcademicProfileIfNeeded(it) }
        if (user != null) {
            Result.Success(user)
        } else {
            Result.Error(Exception("User data not found"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }
}
