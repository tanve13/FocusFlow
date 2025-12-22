package com.tanveer.focusflow.UserInterface.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.tanveer.focusflow.data.model.Task
import com.tanveer.focusflow.viewModel.NotificationViewModel

@Composable
fun PendingTaskChecker(tasks: List<Task>, vm: NotificationViewModel) {
    val currentTime = System.currentTimeMillis()

//    LaunchedEffect(Unit) {
//        tasks.forEach { task ->
//            if (!task.completed && task.deadline != null && task.deadline <= currentTime) {
//                vm.notifyTaskStatus(task)
//            }
//        }
//    }
}
