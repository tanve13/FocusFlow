package com.tanveer.focusflow.data.model

import com.google.firebase.Timestamp

data class FocusSession(
    val id: String = "",
    val userId: String = "",
    val mode: SessionMode = SessionMode.FOCUS,
    val durationSec: Int = 0,
    val completedAt: Timestamp? = null
)

enum class SessionMode { FOCUS, BREAK }
