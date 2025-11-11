package com.android.bbassignment.core.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val isDone: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis() // for merge
)