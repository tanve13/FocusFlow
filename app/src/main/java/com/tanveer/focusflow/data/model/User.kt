package com.tanveer.focusflow.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val streak: Int = 0,
    val totalSessions: Int = 0
)

