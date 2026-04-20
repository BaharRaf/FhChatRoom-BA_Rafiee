package com.example.fhchatroom

import com.google.firebase.firestore.FirebaseFirestore

object Injection {
    private val instance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance().apply {
            persistentCacheIndexManager?.enableIndexAutoCreation()
        }
    }

    fun instance(): FirebaseFirestore {
        return instance
    }
}
