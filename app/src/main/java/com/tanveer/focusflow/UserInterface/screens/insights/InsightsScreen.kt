package com.tanveer.focusflow.UserInterface.screens.insights

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tanveer.focusflow.viewModel.InsightsViewModel
import com.tanveer.focusflow.viewModel.WeeklyData

@Composable
fun InsightsScreen(
    vm: InsightsViewModel = viewModel(),
    uid: String = "current_uid"
) {
    val sessions by vm.dailySessions.collectAsState()
    val tasksCompleted by vm.dailyTasksCompleted.collectAsState()
    val weeklyData by vm.weeklyData.collectAsState()
    val streak by vm.streak.collectAsState(initial = 0)

    LaunchedEffect(uid) { vm.loadData(uid) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        Text(
            "Insights",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        // 🔥 STREAK CARD
        StreakCard(streak)

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatsCardBW(
                title = "Focus Sessions",
                value = sessions.toString()
            )
            StatsCardBW(
                title = "Tasks Done",
                value = tasksCompleted.toString()
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "Weekly Focus",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(12.dp))

        WeeklyGraphBW(weeklyData)
    }
}
@Composable
fun StreakCard(streak: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "🔥",
                fontSize = 32.sp
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Current Streak",
                    color = Color.White.copy(0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "$streak days",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@Composable
fun StatsCardBW(title: String, value: String) {
    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun WeeklyGraphBW(data: List<WeeklyData>) {
    val max = data.maxOfOrNull { it.sessions }?.toFloat() ?: 1f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { item ->
            val heightRatio by animateFloatAsState(
                targetValue = item.sessions / max,
                label = ""
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .width(20.dp)
                        .height(140.dp * heightRatio)
                        .background(
                            Color.Black,
                            RoundedCornerShape(6.dp)
                        )
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    item.day,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                )
            }
        }
    }
}

