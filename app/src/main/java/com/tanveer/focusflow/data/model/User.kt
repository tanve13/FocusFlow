package com.tanveer.focusflow.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val streak: Int = 0,
    val totalSessions: Int = 0,
     val bio : String = "",

)

