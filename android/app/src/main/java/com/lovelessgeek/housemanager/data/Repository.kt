package com.lovelessgeek.housemanager.data

import com.lovelessgeek.housemanager.data.db.TaskEntity

interface Repository {
    suspend fun loadAllTasks(): List<TaskEntity>
    suspend fun addNewTask(task: TaskEntity)
    suspend fun deleteTask(task: TaskEntity)
}