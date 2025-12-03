package com.tanveer.focusflow.data.model


import com.google.firebase.Timestamp

data class Note(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val colorHex: String = "#FFFFFF",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)
