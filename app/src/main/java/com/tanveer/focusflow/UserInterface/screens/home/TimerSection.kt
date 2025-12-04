package com.tanveer.focusflow.ui.screens.home


import androidx.compose.runtime.Composable
import com.tanveer.focusflow.UserInterface.components.CircularTimer

@Composable
fun TimerSection(progress: Float, timeLeft: Int) {
    CircularTimer(progress = progress, timeLeft = timeLeft)
}

