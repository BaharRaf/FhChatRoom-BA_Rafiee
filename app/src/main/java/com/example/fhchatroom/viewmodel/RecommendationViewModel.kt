package com.example.fhchatroom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fhchatroom.Injection
import com.example.fhchatroom.data.User
import com.example.fhchatroom.data.toUserOrNull
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration

data class RecommendationUiState(
    val roomIds: List<String> = emptyList(),
    val generatedAt: Long = 0L,
    val source: String = "NONE"
)

class RecommendationViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = Injection.instance()
    private var listener: ListenerRegistration? = null

    private val _uiState = MutableLiveData(RecommendationUiState())
    val uiState: LiveData<RecommendationUiState> = _uiState

    // Re-attach the Firestore listener whenever the signed-in user changes, so
    // recommendations appear right after login/sign-up without an app restart
    // (and are cleared on logout).
    private val authListener = FirebaseAuth.AuthStateListener {
        observeRecommendations()
    }

    init {
        auth.addAuthStateListener(authListener)
        observeRecommendations()
    }

    private fun observeRecommendations() {
        listener?.remove()
        listener = null

        val email = auth.currentUser?.email
        if (email == null) {
            _uiState.value = RecommendationUiState()
            return
        }

        listener = firestore.collection("users")
            .document(email)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                val user = snapshot?.toUserOrNull() ?: return@addSnapshotListener
                _uiState.value = RecommendationUiState(
                    roomIds = user.recommendedRoomIds,
                    generatedAt = user.recommendationsUpdatedAt,
                    source = user.recommendationSource
                )
            }
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authListener)
        listener?.remove()
        listener = null
    }
}
