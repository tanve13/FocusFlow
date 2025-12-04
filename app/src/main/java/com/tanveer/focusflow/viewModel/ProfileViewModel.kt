package com.tanveer.focusflow.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanveer.focusflow.data.firestore.UserRepository
import com.tanveer.focusflow.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    fun logout(onLogout: () -> Unit) {
        repo.logout()
        onLogout()
    }
}
