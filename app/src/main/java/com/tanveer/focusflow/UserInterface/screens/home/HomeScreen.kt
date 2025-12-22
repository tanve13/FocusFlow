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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tanveer.focusflow.viewModel.HomeViewModel
import com.tanveer.focusflow.viewModel.TaskViewModel
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.navigation.NavHostController
import com.tanveer.focusflow.ui.components.SideSlider
import com.tanveer.focusflow.viewModel.TimerSettingsViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    onOpenMusic: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
     val timerVM: TimerSettingsViewModel = hiltViewModel()
    val focusMinutes by timerVM.focusMinutes.collectAsState(initial = 25)
    val breakMinutes by timerVM.breakMinutes.collectAsState(initial = 5)

    val focusTimeMillis = focusMinutes * 60 * 1000L
    val breakTimeMillis = breakMinutes * 60 * 1000L

    Text("Focus: $focusMinutes min")
    Text("Break: $breakMinutes min")
    val colors = MaterialTheme.colorScheme
    val isRunning by homeViewModel.isRunning.collectAsState()
    val isFocus by homeViewModel.isFocusMode.collectAsState()
    val timeLeft by homeViewModel.timeLeftSec.collectAsState()
    val totalSec =
        if (isFocus) homeViewModel.currentFocusSec()
        else homeViewModel.currentBreakSec()
    var sliderVisible by remember { mutableStateOf(false) }
    val progress = 1f - (timeLeft.toFloat() / totalSec.toFloat())
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 500),
        label = "timerProgress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // ===================== HEADER =====================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colors.primary)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(
                    onClick = { sliderVisible = true },
                    modifier = Modifier
                        .size(40.dp)
                        .background(colors.onPrimary.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = colors.onPrimary
                    )
                }

                Text(
                    text = if (isFocus) "Focus Time" else "Break",
                    color = colors.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(40.dp))
            }
        }

        Spacer(Modifier.height(24.dp))

        // ===================== TIMER =====================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier.size(200.dp),
                    strokeWidth = 12.dp,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )

                Text(
                    text = formatTime(timeLeft),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(Modifier.height(24.dp))

        // ===================== CONTROLS =====================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ControlIcon(
                icon = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                onClick = { homeViewModel.toggleStartStop() }
            )
            ControlIcon(
                icon = Icons.Default.RestartAlt,
                onClick = { homeViewModel.resetTimer() }
            )
            ControlIcon(
                icon = Icons.Default.MusicNote,
                onClick = onOpenMusic
            )
        }

        Spacer(Modifier.height(24.dp))

        // ===================== QUOTE CARD (AS YOU WANT) =====================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                text = "Small steps every day lead to big focus 🤍",
                modifier = Modifier.padding(16.dp),
                color = colors.onSurfaceVariant,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.height(24.dp))

        // ===================== TASKS =====================
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
        },
        openAchievements = {
            sliderVisible = false
            navController.navigate("achievements")
        }
    )

}
@Composable
fun ControlIcon(icon: ImageVector, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(64.dp)
            .background(colors.primary.copy(alpha = 0.08f), CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.primary,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun TodayTasksPreview(taskVm: TaskViewModel = hiltViewModel(),
                      uid: String = "current_uid") {
    val colors = MaterialTheme.colorScheme
    val tasks by taskVm.tasks.collectAsState()
    LaunchedEffect(Unit) { taskVm.loadTasks(uid) }
    Column {
        Text(
            "Today's Tasks",
            fontWeight = FontWeight.Bold,
            color = colors.onBackground
        )

        Spacer(Modifier.height(8.dp))

        tasks.take(3).forEach { t ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = t.completed,
                        onCheckedChange = { }
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        t.title,
                        color = colors.onSurface
                    )
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
