package com.tanveer.focusflow.data.model

data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val imageRes: Int,
    val unlocked: Boolean,
    val progress: Float = 0f
)

