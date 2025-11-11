package com.android.bbassignment.feature.taskboard.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.bbassignment.R
import com.android.bbassignment.core.data.Task
import com.android.bbassignment.feature.taskboard.viewModel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    onAdd: () -> Unit,
    onEdit: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TaskBoard") },
                actions = {
                    IconButton(onClick = { viewModel.sync() }) {
                        if (state.isSyncing)
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        else
                            Icon(Icons.Default.Refresh, contentDescription = "Sync")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->

        // 1️⃣ Loading indicator
        if (state.isLoading) {
            Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        // 2️⃣ Empty State View
        if (state.tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_empty_tasks),
                        contentDescription = "No tasks",
                        modifier = Modifier
                            .size(160.dp)
                            .padding(bottom = 16.dp)
                    )
                    Text(
                        text = "No tasks added yet",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap + to create your first task!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            return@Scaffold
        }

        // 3️⃣ Task List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(count = state.tasks.size) { index ->
                val task = state.tasks[index]
                TaskRow(
                    task = task,
                    onToggle = { viewModel.toggleDone(task) },
                    onDelete = { viewModel.delete(task) },
                    onEdit = { onEdit(task.id) }
                )
            }
        }
    }
}

@Composable
fun TaskRow(task: Task, onToggle: () -> Unit, onDelete: () -> Unit, onEdit: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = task.isDone, onCheckedChange = { onToggle() })
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onEdit() }
        ) {
            Text(task.title, style = MaterialTheme.typography.titleMedium)
            if (task.description.isNotEmpty()) {
                Text(task.description, style = MaterialTheme.typography.bodySmall)
            }
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}
