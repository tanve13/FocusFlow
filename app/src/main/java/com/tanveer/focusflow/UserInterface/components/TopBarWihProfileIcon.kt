package com.tanveer.focusflow.UserInterface.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle

@Composable
fun TopBarWithProfileIcon(
    title: String = "FocusFlow",
    onProfileClick: () -> Unit
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF6C63FF), Color(0xFF8576FF))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(gradient)
            .padding(horizontal = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(start = 4.dp, top = 12.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge, color = Color.White)
            }

            IconButton(onClick = onProfileClick, modifier = Modifier.padding(end = 4.dp)) {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Profile", tint = Color.White)
            }
        }
    }
}
