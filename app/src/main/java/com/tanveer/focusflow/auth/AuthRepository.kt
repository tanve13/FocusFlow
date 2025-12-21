package com.tanveer.focusflow.auth


import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // REGISTER NEW USER
    fun register(name: String, email: String, pass: String, onDone: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                val uid = it.user!!.uid

                val userData = mapOf(
                    "uid" to uid,
                    "name" to name,
                    "email" to email,
                    "photoUrl" to ""
                )

                db.collection("users").document(uid).set(userData)
                onDone(true, null)
            }
            .addOnFailureListener { e ->
                onDone(false, e.message)
            }
    }

    // LOGIN USER
    fun login(email: String, pass: String, onDone: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(
            email.trim(),
            pass.trim()
        )
            .addOnSuccessListener { onDone(true, null) }
            .addOnFailureListener { e -> onDone(false, e.message) }
        Log.d("AUTH_DEBUG", "Email = '$email'")

    }


    // FORGOT PASSWORD
    fun forgotPassword(email: String, onDone: (Boolean, String?) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onDone(true, null) }
            .addOnFailureListener { e -> onDone(false, e.message) }
    }

    fun getCurrentUser() = auth.currentUser
}
