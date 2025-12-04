package com.tanveer.focusflow.UserInterface.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tanveer.focusflow.ui.components.SideSlider
import com.tanveer.focusflow.viewModel.HomeViewModel
import com.tanveer.focusflow.viewModel.TaskViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    onOpenMusic: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val isRunning by homeViewModel.isRunning.collectAsState()
    val isFocus by homeViewModel.isFocusMode.collectAsState()
    val timeLeft by homeViewModel.timeLeftSec.collectAsState()
    val showBreathing = isRunning && isFocus
    var sliderVisible by remember { mutableStateOf(false) }

    val totalSec = if (isFocus) homeViewModel.currentFocusSec() else homeViewModel.currentBreakSec()
    val progress = 1f - (timeLeft.toFloat() / totalSec.toFloat())


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF7F7FB))
            .padding(16.dp)
    ) {

        // ===================== Top Bar with Gradient =====================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF6C63FF), Color(0xFF8A84FF))
                    )
                )
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Profile Icon on left
                IconButton(
                    onClick = { sliderVisible = true },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White
                    )
                }

                Text(
                    text = if (isFocus) "Focus Time" else "Break",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(40.dp)) // Placeholder for right side
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ===================== Timer Section =====================
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            BreathingAnimation(visible = showBreathing)

            CircularProgressIndicator(
                progress = progress.coerceIn(0f, 1f),
                modifier = Modifier.size(200.dp),
                strokeWidth = 12.dp,
                color = Color(0xFF6C63FF)
            )
            Text(
                text = formatTime(timeLeft),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = if (isFocus) Color(0xFF6C63FF) else Color(0xFF8A84FF)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ===================== Controls Row =====================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ControlButton(
                icon = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                description = "Start",
                onClick = { homeViewModel.toggleStartStop() }
            )

            ControlButton(
                icon = Icons.Default.RestartAlt,
                description = "Reset",
                onClick = { homeViewModel.resetTimer() }
            )

            ControlButton(
                icon = Icons.Default.MusicNote,
                description = "Music",
                onClick = onOpenMusic
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ===================== Quote Section =====================
        QuoteSection()

        Spacer(modifier = Modifier.height(24.dp))

        // ===================== Today Tasks =====================
        TodayTasksPreview()
    }
    SideSlider(
        visible = sliderVisible,
        onClose = { sliderVisible = false },
        openProfile = {
            sliderVisible = false
            navController.navigate("profile")
        },
        openSettings = {
            sliderVisible = false

            navController.navigate("settings")
        }
    )
}

@Composable
fun ControlButton(icon: androidx.compose.ui.graphics.vector.ImageVector, description: String, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(64.dp)
            .background(Color(0xFF6C63FF).copy(alpha = 0.1f), CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = Color(0xFF6C63FF),
            modifier = Modifier.size(32.dp)
        )
    }
}


@Composable
fun TodayTasksPreview(taskVm: TaskViewModel = hiltViewModel(), uid: String = "current_uid") {
    val tasks by taskVm.tasks.collectAsState()
    LaunchedEffect(Unit) { taskVm.loadTasks(uid) }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp)) {
        Text("Today's Tasks", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
        tasks.take(3).forEach { t ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = t.completed,
                        onCheckedChange = { taskVm.updateTask(uid, t.copy(completed = !t.completed)) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(t.title, maxLines = 1, fontWeight = FontWeight.Medium)
                        if (t.description.isNotBlank()) Text(t.description, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

fun formatTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "%02d:%02d".format(min, sec)
}
