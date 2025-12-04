package com.tanveer.focusflow.UserInterface.screens.home


import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

private val quotes = listOf(
    "Small steps, big changes.",
    "Focus on progress, not perfection.",
    "One session at a time.",
    "Start now. Perfect later."
)

@Composable
fun QuoteSection() {
    var idx by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(6000)
            idx = (idx + 1) % quotes.size
        }
    }

    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp)) {
        Crossfade(targetState = idx) { i ->
            Text(
                text = quotes[i],
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
    }
}
