package com.tanveer.focusflow.UserInterface.screens.music

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmbientSoundScreen(onBack: () -> Unit) {
    val sounds = listOf("Rain", "Forest", "Café", "Waves", "White Noise", "Lo-fi")
    Scaffold(topBar = { TopAppBar(title = { Text("Ambient Sounds") }, navigationIcon = { IconButton(onClick = onBack) { Icon(
        Icons.Default.ArrowBack, "") } }) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(sounds) { s ->
                Card(modifier = Modifier.fillMaxWidth().clickable { /* play/pause via ExoPlayer */ }) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(s)
                        Text("Preview", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}

