package com.tanveer.focusflow.data.model


data class Notification(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

