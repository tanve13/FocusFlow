package com.tanveer.focusflow.data.firestore

import com.tanveer.focusflow.model.FocusSession
import com.google.firebase.firestore.FirebaseFirestore
import com.tanveer.focusflow.data.model.FocusSession
import kotlinx.coroutines.tasks.await

class SessionRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    fun sessionCollection(uid: String) = db.collection("sessions").document(uid).collection("history")

    suspend fun saveSession(uid: String, session: FocusSession) {
        val doc = sessionCollection(uid).document()
        sessionCollection(uid).document(doc.id).set(session.copy(id = doc.id)).await()
    }
}

