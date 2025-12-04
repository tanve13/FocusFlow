package com.tanveer.focusflow.UserInterface.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text

@Composable
fun BreathingAnimation(visible: Boolean) {
    if (!visible) return

    val scale = remember { Animatable(0.6f) }
    LaunchedEffect(Unit) {
        scale.animateTo(1.05f, tween(900, easing = EaseInOut))
        scale.animateTo(0.6f, tween(900, easing = EaseInOut))
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .scale(scale.value)
                .background(Color(0xFF6C63FF).copy(alpha = 0.12f), CircleShape)
        )
    }
}
