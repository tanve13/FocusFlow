package com.tanveer.focusflow.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CircularTimer(progress: Float, timeLeft: Int) {
    val anim = animateFloatAsState(targetValue = progress).value
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(260.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 20f)
            val radius = size.minDimension / 2
            drawCircle(color = MaterialTheme.colorScheme.surface, radius = radius, style = stroke)
            drawArc(color = MaterialTheme.colorScheme.primary, startAngle = -90f, sweepAngle = 360f * anim, useCenter = false, style = stroke)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val m = timeLeft / 60
            val s = timeLeft % 60
            Text(String.format("%02d:%02d", m, s), style = MaterialTheme.typography.titleLarge.copy(fontSize = 34.sp, fontWeight = FontWeight.SemiBold))
            Spacer(Modifier.height(4.dp))
            Text("Remaining", style = MaterialTheme.typography.bodySmall)
        }
    }
}
