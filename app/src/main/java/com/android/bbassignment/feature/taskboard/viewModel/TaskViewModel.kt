package com.android.bbassignment.feature.taskboard.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.bbassignment.core.data.Task
import com.android.bbassignment.core.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repo: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.observeTasks().collect { list ->
                _uiState.update { it.copy(tasks = list, isLoading = false) }
            }
        }
    }

    fun addTask(title: String, desc: String) = viewModelScope.launch {
        if (title.isBlank()) {
            _uiState.update { it.copy(error = "Title required") }
            return@launch
        }
        repo.insertTask(Task(title = title, description = desc))
    }

    fun updateTask(task: Task, newTitle: String, newDesc: String) = viewModelScope.launch {
        repo.updateTask(task.copy(title = newTitle, description = newDesc))
    }

    fun toggleDone(task: Task) = viewModelScope.launch {
        repo.updateTask(task.copy(isDone = !task.isDone, updatedAt = System.currentTimeMillis()))
    }

    fun delete(task: Task) = viewModelScope.launch { repo.delete(task) }

    fun sync() = viewModelScope.launch {
        _uiState.update { it.copy(isSyncing = true) }
        try {
            repo.syncWithNetwork()
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message ?: "Sync failed") }
        } finally {
            _uiState.update { it.copy(isSyncing = false) }
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
}

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
    val error: String? = null
)
