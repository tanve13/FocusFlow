package com.tanveer.focusflow.data.firestore


import com.tanveer.focusflow.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TaskRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
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
