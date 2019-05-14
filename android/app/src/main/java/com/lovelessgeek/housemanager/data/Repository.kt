package com.lovelessgeek.housemanager.data

import com.lovelessgeek.housemanager.data.db.TaskEntity

interface Repository {
    fun loadAllTasks(): List<TaskEntity>
    fun addNewTask(task: TaskEntity)
    fun deleteTask(task: TaskEntity)
}