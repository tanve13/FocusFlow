package com.tanveer.focusflow.viewModel

import com.google.firebase.firestore.FirebaseFirestore
import com.tanveer.focusflow.data.model.Badge
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BadgeRepository @Inject constructor(private val db: FirebaseFirestore) {

    suspend fun getUserBadges(uid: String): List<Badge> {
        val snapshot = db.collection("users").document(uid).collection("badges").get().await()
        return snapshot.documents.mapNotNull { it.toObject(Badge::class.java) }
    }

    suspend fun unlockBadge(uid: String, badgeId: String) {
        db.collection("users").document(uid)
            .collection("badges").document(badgeId)
            .update("unlocked", true).await()
    }
}
