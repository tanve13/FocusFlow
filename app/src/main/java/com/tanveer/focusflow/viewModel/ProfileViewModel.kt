package com.tanveer.focusflow.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanveer.focusflow.data.datastore.CloudinaryHelper
import com.tanveer.focusflow.data.firestore.UserRepository
import com.tanveer.focusflow.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun loadUser(uid: String) {
        viewModelScope.launch {
            _user.value = repo.getUser(uid)
        }
    }

    fun updateUser(uid: String, updatedUser: User, onComplete: () -> Unit) {
        viewModelScope.launch {
            repo.updateUser(uid, updatedUser)
            loadUser(uid)
            onComplete()
        }
    }
    fun updateProfile(uid: String, name: String, bio: String) {
        viewModelScope.launch {
            val updated = user.value?.copy(name = name, bio = bio)
            if (updated != null) {
                repo.updateUser(uid, updated)
                _user.value = updated
            }
        }
    }
    fun uploadProfileImage(context: Context, imageUri: Uri, uid: String) {

        val file = uriToFile(context, imageUri)

        CloudinaryHelper.uploadImage(file) { success, url ->
            Log.d("UPLOAD", "success=$success url=$url")

            if (success && url != null) {
                viewModelScope.launch {
                    Log.d("UPLOAD", "Saving URL to Firestore")

                    // ✅ ONLY update photoUrl
                    repo.updateProfileImage(uid, url)

                    // ✅ update local state so UI refreshes
                    _user.value = _user.value?.copy(photoUrl = url)

                    Log.d("UPLOAD", "Profile image updated successfully")
                }
            }
        }
    }

    fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)!!
        val file = File(context.cacheDir, "profile_${System.currentTimeMillis()}.jpg")
        val outputStream = file.outputStream()

        inputStream.copyTo(outputStream)

        inputStream.close()
        outputStream.close()

        return file
    }


    fun logout(onLogout: () -> Unit) {
        repo.logout()
        onLogout()
    }
}
