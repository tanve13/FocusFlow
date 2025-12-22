package com.tanveer.focusflow.data.model

import com.google.firebase.Timestamp

data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val dueAt: Timestamp? = null,
    val completed: Boolean = false,
    val createdAt: Timestamp? = null,
    val deadline: Long? = null // timestamp in millis
)

enum class Priority { HIGH, MEDIUM, LOW }
