package com.tanveer.focusflow.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(
    val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // LOGIN FUNCTION
    fun login(email: String, pass: String, onSuccess: () -> Unit) {

        if (email.isBlank() || pass.isBlank()) {
            _error.value = "Email or Password cannot be empty"
            return
        }

        _loading.value = true
        repo.login(email, pass) { ok, msg ->
            _loading.value = false
            if (ok) onSuccess() else _error.value = msg
        }
    }


    // REGISTER FUNCTION
    fun register(name: String, email: String, pass: String, onSuccess: () -> Unit) {
        _loading.value = true
        repo.register(name, email, pass) { ok, msg ->
            _loading.value = false
            if (ok) onSuccess() else _error.value = msg
        }
    }

    // FORGOT PASSWORD
    fun forgotPassword(email: String) {
        repo.forgotPassword(email) { ok, msg ->
            if (!ok) _error.value = msg
        }
    }
}
