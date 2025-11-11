package com.android.bbassignment.core.repository

import com.android.bbassignment.core.data.Task
import com.android.bbassignment.core.data.TaskDao
import com.android.bbassignment.core.network.DummyNetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val dao: TaskDao,
    private val network: DummyNetworkService
) {

    fun observeTasks(): Flow<List<Task>> = dao.getAll()

    suspend fun insertTask(task: Task) {
        dao.insert(task.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun updateTask(task: Task) {
        dao.insert(task.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun delete(task: Task) = dao.delete(task)

    suspend fun syncWithNetwork() {
        val remote = network.fetchTasks()
        val local = dao.getAll().first()
        val localMap = local.associateBy { it.id }.toMutableMap()

        remote.forEach { r ->
            val l = localMap[r.id]
            if (l == null) {
                dao.insert(r.copy(updatedAt = System.currentTimeMillis()))
            } else {
                val merged = if (r.updatedAt > l.updatedAt) {
                    r.copy(isDone = l.isDone, updatedAt = System.currentTimeMillis())
                } else l
                dao.insert(merged)
            }
        }
    }
}
