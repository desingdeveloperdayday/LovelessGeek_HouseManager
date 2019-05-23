package com.lovelessgeek.housemanager.data

import com.lovelessgeek.housemanager.data.db.LocalDatabase
import com.lovelessgeek.housemanager.data.db.TaskDao
import com.lovelessgeek.housemanager.data.db.TaskMapper
import com.lovelessgeek.housemanager.shared.models.Task

class RepositoryImpl(db: LocalDatabase) : Repository {

    private val taskDao: TaskDao = db.getTaskDao()

    override suspend fun loadAllTasks(): List<Task> {
        return taskDao.loadAllTasks().map {
            TaskMapper.fromEntity(it)
        }
    }

    override suspend fun addNewTask(task: Task) {
        taskDao.insert(TaskMapper.toEntity(task))
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(TaskMapper.toEntity(task))
    }
}