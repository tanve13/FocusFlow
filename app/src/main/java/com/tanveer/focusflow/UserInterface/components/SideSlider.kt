package com.tanveer.focusflow.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SideSlider(visible: Boolean, onClose: () -> Unit, openProfile: () -> Unit = {}, openSettings: () -> Unit = {}) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(180)) + slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)),
        exit = fadeOut(tween(180)) + slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable { onClose() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(320.dp)
                    .align(Alignment.CenterEnd)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                    .padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text("Profile", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text("Tap below to open profile or settings", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                Spacer(Modifier.height(16.dp))

                SliderRow(text = "View Profile") { openProfile() }
                SliderRow(text = "Settings") { openSettings() }
                SliderRow(text = "Achievements") { /* navigate */ }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { /* logout */ }, modifier = Modifier.fillMaxWidth()) { Text("Logout") }
            }
        }
    }
}

@Composable
private fun SliderRow(text: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 12.dp)) {
        Text(text, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Open $text", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}
