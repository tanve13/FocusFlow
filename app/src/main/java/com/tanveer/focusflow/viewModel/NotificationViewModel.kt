package com.tanveer.focusflow.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanveer.focusflow.data.firestore.NotificationRepository
import com.tanveer.focusflow.data.model.Notification
import com.tanveer.focusflow.data.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.onesignal.OneSignal
import com.tanveer.focusflow.data.datastore.NotificationHelper
import org.json.JSONArray
import org.json.JSONObject
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repo: NotificationRepository
) : ViewModel() {

    val notifications: StateFlow<List<Notification>> = repo.notifications
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addNotification(notification: Notification) {
        viewModelScope.launch { repo.addNotification(notification) }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch { repo.deleteNotification(notificationId) }
    }

    fun notifyTaskStatus(task: Task, context: Context) {

        val (emoji, title) = when {
            task.completed -> "✅" to "Task Completed"
            task.deadline != null && task.deadline < System.currentTimeMillis() ->
                "⚠️" to "Task Deadline Passed"
            else -> "⏳" to "Task Pending"
        }

        val content = "$emoji ${task.title}"

        val notification = Notification(title = title, content = content)

        // 🔹 Notification screen ke liye
        addNotification(notification)

        // 🔹 SYSTEM notification
        NotificationHelper.show(context, title, content)
    }






}
