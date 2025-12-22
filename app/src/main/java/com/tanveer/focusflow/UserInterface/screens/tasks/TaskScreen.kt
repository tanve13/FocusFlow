package com.tanveer.focusflow.UserInterface.screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tanveer.focusflow.data.model.Task
import com.tanveer.focusflow.viewModel.NotificationViewModel
import com.tanveer.focusflow.viewModel.TaskViewModel

@Composable
fun TaskScreen(
    vm: TaskViewModel = hiltViewModel(),
    uid: String = "current_uid",
    onEditTask: (Task) -> Unit = {},
) {
    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val tasks by vm.tasks.collectAsState()
    LaunchedEffect(Unit) { vm.loadTasks(uid) }

    var showDialog by remember { mutableStateOf(false) }
    var filter by remember { mutableStateOf("All") }
    var showAddDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    val filteredTasks = tasks.filter { task ->
        when (filter) {
            "Completed" -> task.completed
            "Pending" -> !task.completed
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tasks", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("${tasks.count { it.completed }}/ ${tasks.size} Completed",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary)
        }

        Spacer(Modifier.height(16.dp))

        // FILTER ROW
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("All", "Completed", "Pending").forEach { f ->
                Button(
                    onClick = { filter = f },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (filter == f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        contentColor = if (filter == f) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                ) { Text(f) }
            }
        }

        Spacer(Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredTasks) { task ->
                    TaskCard(
                        task = task,
                        vm = notificationViewModel,
                        onEdit = { taskToEdit = task },
                        onDelete = { taskToDelete = task },
                        onTaskChecked = { updatedTask ->
                            vm.updateTask(uid, updatedTask)
                        }
                    )
                }
            }

            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) { Text("+", color = Color.White, style = MaterialTheme.typography.headlineSmall) }
        }

        if (showDialog) {
            InputDialog(
                title = "Add New Task",
                placeholder = "Enter task title",
                onConfirm = { text ->
                    if (text.isNotBlank()) vm.addTask(uid, Task(title = text, description = ""))
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
        // ------------------- Edit Task -------------------
        taskToEdit?.let { task ->
            InputDialog(
                title = "Edit Task",
                placeholder = "Enter task title",
                initialText = task.title,
                onConfirm = { text ->
                    if (text.isNotBlank()) vm.updateTask(uid, task.copy(title = text))
                    taskToEdit = null
                },
                onDismiss = { taskToEdit = null }
            )
        }
        // ------------------- Delete Confirmation -------------------
        taskToDelete?.let { task ->
            AlertDialog(
                onDismissRequest = { taskToDelete = null },
                title = { Text("Delete Task") },
                text = { Text("Are you sure you want to delete this task?") },
                confirmButton = {
                    TextButton(onClick = {
                        vm.deleteTask(uid, task.id)
                        taskToDelete = null
                    }) { Text("Yes") }
                },
                dismissButton = {
                    TextButton(onClick = { taskToDelete = null }) { Text("No") }
                }
            )
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onTaskChecked: (Task) -> Unit,
    vm: NotificationViewModel
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ✅ CHECKBOX
            Checkbox(
                checked = task.completed,
                onCheckedChange = {checked ->
                    onTaskChecked(task.copy(completed = checked))

                    vm.notifyTaskStatus(
                        task.copy(completed = checked),
                        context
                    )
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // ✅ TASK TITLE
            Text(
                task.title,
                modifier = Modifier.weight(1f)
            )

            // ✏️ EDIT
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }

            // 🗑 DELETE
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}



@Composable
fun InputDialog(
    title: String,
    placeholder: String,
    initialText: String = "",
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(initialText) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) { Text("Cancel") }
        },
        title = { Text(title) },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(placeholder) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

