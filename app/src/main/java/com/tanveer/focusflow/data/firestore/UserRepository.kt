package com.tanveer.focusflow.data.firestore

import android.content.Context
import android.net.Uri
import com.tanveer.focusflow.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
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
    suspend fun uploadProfileImage(context: Context, imageUri: Uri, uid: String): String {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bytes = inputStream!!.readBytes()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                "image.jpg",
                bytes.toRequestBody("image/*".toMediaType())
            )
            .addFormDataPart("upload_preset", "upload_preset") // tumhara preset
            .addFormDataPart("folder", "focusflow") // optional
            .build()

        val request = Request.Builder()
            .url("https://api.cloudinary.com/v1_1/dyeywm7b5/image/upload")
            .post(requestBody)
            .build()

        val response = OkHttpClient().newCall(request).execute()
        val json = JSONObject(response.body!!.string())
        return json.getString("secure_url")
    }

    // Logout the current user
    fun logout() {
        auth.signOut()
    }
}
