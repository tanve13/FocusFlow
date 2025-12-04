package com.tanveer.focusflow.data.firestore

import com.tanveer.focusflow.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private fun userDoc(uid: String) = db.collection("users").document(uid)

    // Create or overwrite user
    suspend fun upsertUser(user: User) {
        userDoc(user.uid).set(user).await()
    }

    // Get user data
    suspend fun getUser(uid: String): User? {
        val snap = userDoc(uid).get().await()
        return if (snap.exists()) snap.toObject(User::class.java) else null
    }

    // Update user fields (like name, email)
    suspend fun updateUser(uid: String, user: User) {
        userDoc(uid).set(user).await() // Overwrite the user document
    }

    // Update streak and total sessions
    suspend fun updateStreak(uid: String, streak: Int, totalSessions: Int) {
        userDoc(uid).update("streak", streak, "totalSessions", totalSessions).await()
    }

    // Logout the current user
    fun logout() {
        auth.signOut()
    }
}
