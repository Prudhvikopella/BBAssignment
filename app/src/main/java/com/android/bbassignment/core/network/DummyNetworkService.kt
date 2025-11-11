package com.android.bbassignment.core.network

import com.android.bbassignment.core.data.Task
import kotlinx.coroutines.delay
import javax.inject.Inject

class DummyNetworkService @Inject constructor() {
    suspend fun fetchTasks(): List<Task> {
        delay(1500) // simulate network
        return listOf(
            Task(id = "net1", title = "Buy milk", description = "From the store", isDone = false),
            Task(id = "net2", title = "Call mom", description = "", isDone = true)
        )
    }
}
