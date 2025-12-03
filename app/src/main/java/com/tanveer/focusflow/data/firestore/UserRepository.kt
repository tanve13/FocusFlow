package com.tanveer.focusflow.data.firestore


import com.tanveer.focusflow.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    private fun userDoc(uid: String) = db.collection("users").document(uid)

    suspend fun upsertUser(user: User) {
        userDoc(user.uid).set(user).await()
    }

    suspend fun getUser(uid: String): User? {
        val snap = userDoc(uid).get().await()
        return if (snap.exists()) snap.toObject(User::class.java) else null
    }

    suspend fun updateStreak(uid: String, streak: Int, totalSessions: Int) {
        userDoc(uid).update("streak", streak, "totalSessions", totalSessions).await()
    }
}
