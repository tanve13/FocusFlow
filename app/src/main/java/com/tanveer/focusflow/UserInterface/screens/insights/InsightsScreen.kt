package com.tanveer.focusflow.UserInterface.screens.insights

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tanveer.focusflow.viewModel.InsightsViewModel
import com.tanveer.focusflow.viewModel.WeeklyData

@Composable
fun InsightsScreen(vm: InsightsViewModel = viewModel(), uid: String = "current_uid") {
    val sessions by vm.dailySessions.collectAsState()
    val tasksCompleted by vm.dailyTasksCompleted.collectAsState()
    val weeklyData by vm.weeklyData.collectAsState()

    // Load mock data for now
    LaunchedEffect(Unit) { vm.loadData(uid) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Insights", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        StatsCard(
            title = "Today's Focus Sessions",
            value = sessions.toString(),
            gradient = Brush.horizontalGradient(listOf(Color(0xFF6C63FF), Color(0xFF9D5CFF)))
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatsCard(
            title = "Tasks Completed Today",
            value = tasksCompleted.toString(),
            gradient = Brush.horizontalGradient(listOf(Color(0xFF42A5F5), Color(0xFF478DE0)))
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Weekly Focus Progress", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(12.dp))

        WeeklyGraph(
            data = weeklyData,
            barColor = Color(0xFF6C63FF)
        )
    }
}

@Composable
fun StatsCard(title: String, value: String, gradient: Brush) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = value,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun WeeklyGraph(
    data: List<WeeklyData>,
    barColor: Color = Color(0xFF6C63FF)
) {
    val maxValue = data.maxOfOrNull { it.sessions }?.toFloat() ?: 1f
    val barWidthDp = 24.dp
    val spacingDp = 16.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { item ->
            val animatedHeight = animateFloatAsState(
                targetValue = item.sessions / maxValue,
                label = ""
            ).value

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                // Bar
                Box(
                    modifier = Modifier
                        .height(160.dp * animatedHeight)
                        .width(barWidthDp)
                        .background(
                            color = barColor,
                            shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                        )
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Day label
                Text(
                    text = item.day,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
