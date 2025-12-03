package com.tanveer.focusflow.UserInterface.screens.insights


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tanveer.focusflow.viewModel.InsightsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun InsightsScreen(vm: InsightsViewModel = viewModel(), uid: String = "current_uid") {
    val sessions by vm.dailySessions.collectAsState()
    val tasksCompleted by vm.dailyTasksCompleted.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Insights", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        StatsCard(title = "Today's Focus Sessions", value = sessions.toString())
        Spacer(Modifier.height(8.dp))
        StatsCard(title = "Tasks Completed Today", value = tasksCompleted.toString())
        Spacer(Modifier.height(12.dp))
        WeeklyGraph() // stub - create visual bar chart using Canvas later
    }
}
