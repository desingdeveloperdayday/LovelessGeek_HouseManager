package com.lovelessgeek.housemanager.data

import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Task

interface Repository {
    suspend fun loadAllTasks(): List<Task>
    suspend fun addNewTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun loadCategories(): List<Category>

    suspend fun deleteAll()
}