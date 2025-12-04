package com.tanveer.focusflow.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanveer.focusflow.data.firestore.TaskRepository
import com.tanveer.focusflow.data.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repo: TaskRepository) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    fun loadTasks(uid: String) {
        viewModelScope.launch {
            try {
                val t = repo.getAllTasks(uid)
                _tasks.value = t
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun addTask(uid: String, task: Task) {
        viewModelScope.launch {
            repo.addTask(uid, task)
            loadTasks(uid)
        }
    }

    fun updateTask(uid: String, task: Task) {
        viewModelScope.launch {
            repo.updateTask(uid, task)
            loadTasks(uid)
        }
    }

    fun deleteTask(uid: String, taskId: String) {
        viewModelScope.launch {
            repo.deleteTask(uid, taskId)
            loadTasks(uid)
        }
    }
}
