package com.tanveer.focusflow.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tanveer.focusflow.viewModel.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tanveer.focusflow.ui.components.SideSlider
import com.tanveer.focusflow.ui.components.BottomNavigationBar
import com.tanveer.focusflow.ui.components.SideSlider
import com.tanveer.focusflow.ui.components.AppTopBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.RestartAlt
import com.tanveer.focusflow.UserInterface.screens.home.CircularTimer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenMusic: () -> Unit,
    onOpenProfile: () -> Unit,
    vm: HomeViewModel = viewModel()
) {
    val isRunning by vm.isRunning.collectAsState()
    val isFocus by vm.isFocusMode.collectAsState()
    val timeLeft by vm.timeLeftSec.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = { Text(if (isFocus) "Focus Time" else "Break") },
            actions = {
                IconButton(onClick = onOpenProfile) { Icon(Icons.Default.MusicNote, contentDescription = "Profile") }
            }
        )
        Spacer(Modifier.height(12.dp))
        CircularTimer(progress = 1f - (timeLeft.toFloat() / (if (isFocus) 25*60 else 5*60)), timeLeft = timeLeft)
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconButton(
                onClick = { vm.toggleStartStop() },
                modifier = Modifier.size(64.dp)
            ) {
                Icon(if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = "Start/Stop")
            }
            IconButton(onClick = { vm.resetTimer() }, modifier = Modifier.size(64.dp)) {
                Icon(Icons.Default.RestartAlt, contentDescription = "Reset")
            }
            IconButton(onClick = onOpenMusic, modifier = Modifier.size(64.dp)) {
                Icon(Icons.Default.MusicNote, contentDescription = "Music")
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("Tip: Use small 25-min sessions and 5-min breaks", style = MaterialTheme.typography.bodyMedium)
    }
}
