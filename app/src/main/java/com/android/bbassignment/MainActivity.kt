package com.android.bbassignment

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.bbassignment.feature.taskboard.ui.TaskListScreen
import com.android.bbassignment.feature.taskboard.viewModel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TaskBoardAppNav() }
    }
}


@Composable
fun TaskBoardAppNav() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "list") {
        composable("list") {
            TaskListScreen(
                onAdd = { navController.navigate("add_edit") },
                onEdit = { id -> navController.navigate("add_edit/$id") }
            )
        }
        composable("add_edit") {
            AddEditTaskScreen(taskId = null) { navController.popBackStack() }
        }
        composable("add_edit/{taskId}") { backStack ->
            val id = backStack.arguments?.getString("taskId")
            AddEditTaskScreen(taskId = id) { navController.popBackStack() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    taskId: String?,
    viewModel: TaskViewModel = hiltViewModel(),
    onDone: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val existingTask = remember(state.tasks) {
        state.tasks.find { it.id == taskId }
    }

    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }

    // Prefill existing data once
    LaunchedEffect(existingTask) {
        if (existingTask != null && title.isBlank() && description.isBlank()) {
            title = existingTask.title
            description = existingTask.description
        }
    }

    val isChanged = remember(title, description) {
        existingTask?.let {
            title.trim() != it.title.trim() || description.trim() != it.description.trim()
        } ?: (title.isNotBlank() || description.isNotBlank())
    }

    val isEnabled = isChanged && title.isNotBlank()

    // Listen for snackbar messages
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingTask == null) "Add Task" else "Edit Task") },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { androidx.compose.material3.SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when {
                        title.isBlank() -> {
                            snackbarMessage = "Title cannot be empty"
                        }
                        existingTask == null -> {
                            viewModel.addTask(title.trim(), description.trim())
                            //snackbarMessage = "Task added successfully"
                            onDone()
                        }
                        isChanged -> {
                            viewModel.updateTask(existingTask, title.trim(), description.trim())
                            //snackbarMessage = "Task updated successfully"
                            onDone()
                        }
                        else -> {
                            snackbarMessage = "No changes to save"
                        }
                    }
                },
                containerColor = if (isEnabled)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isEnabled)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.alpha(if (isEnabled) 1f else 0.5f)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = title.isBlank()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                maxLines = 5
            )
        }
    }
}






