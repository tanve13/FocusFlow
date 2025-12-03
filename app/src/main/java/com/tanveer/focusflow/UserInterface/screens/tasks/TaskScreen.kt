package com.tanveer.focusflow.UserInterface.screens.tasks

import com.tanveer.focusflow.data.model.Task

package com.tanveer.focusflow.ui.screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tanveer.focusflow.model.Task
import com.tanveer.focusflow.viewModel.TaskViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tanveer.focusflow.ui.components.PriorityChip

@Composable
fun TaskScreen(vm: TaskViewModel = viewModel(), uid: String = "current_uid") {
    val tasks by vm.tasks.collectAsState()
    LaunchedEffect(Unit) { vm.loadTasks(uid) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Tasks", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(tasks) { task ->
                TaskItem(task = task, onToggle = {
                    vm.updateTask(uid, it.copy(completed = !it.completed))
                }, onDelete = {
                    vm.deleteTask(uid, it.id)
                })
            }
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            // show add sheet -> simple add quick demo:
            val t = Task(title = "Quick Task ${System.currentTimeMillis()}", description = "Add details")
            vm.addTask(uid, t)
        }) { Text("Add Quick Task") }
    }
}
