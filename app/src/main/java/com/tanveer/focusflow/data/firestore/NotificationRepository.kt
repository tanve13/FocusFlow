package com.tanveer.focusflow.data.firestore

import com.tanveer.focusflow.data.model.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    fun addNotification(notification: Notification) {
        _notifications.value = _notifications.value + notification
    }

    fun deleteNotification(notificationId: String) {
        _notifications.value = _notifications.value.filter { it.id != notificationId }
    }

    fun loadNotifications(): List<Notification> {
        return _notifications.value
    }
}
