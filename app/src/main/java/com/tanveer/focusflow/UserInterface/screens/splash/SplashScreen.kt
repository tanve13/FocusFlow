package com.tanveer.focusflow.UserInterface.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {

    // simple scale animation
    val scale = remember { Animatable(0.6f) }

    LaunchedEffect(true) {
        scale.animateTo(
            1f,
            animationSpec = tween(durationMillis = 1200,
                easing = { fraction -> OvershootInterpolator(2f).getInterpolation(fraction) })
        )
        delay(800)
        onFinished()
    }

    // App brand gradient
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF6C63FF), Color(0xFF8576FF))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value)
                    .background(Color.White.copy(alpha = 0.15f), CircleShape)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                "FocusFlow",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                letterSpacing = 2.sp
            )
        }
    }
}
