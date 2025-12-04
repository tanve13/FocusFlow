package com.tanveer.focusflow.data.firestore


import com.google.firebase.firestore.FirebaseFirestore
import com.tanveer.focusflow.data.model.Task
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    fun tasksCollection(uid: String) = db.collection("tasks").document(uid).collection("userTasks")

    suspend fun addTask(uid: String, task: Task) {
        val doc = tasksCollection(uid).document()
        tasksCollection(uid).document(doc.id).set(task.copy(id = doc.id)).await()
    }

    suspend fun updateTask(uid: String, task: Task) {
        tasksCollection(uid).document(task.id).set(task).await()
    }

    suspend fun deleteTask(uid: String, taskId: String) {
        tasksCollection(uid).document(taskId).delete().await()
    }

    suspend fun getAllTasks(uid: String) =
        tasksCollection(uid).get().await().documents.mapNotNull { it.toObject(Task::class.java) }
}

