package com.lovelessgeek.housemanager.data

import com.lovelessgeek.housemanager.data.db.LocalDatabase
import com.lovelessgeek.housemanager.data.db.TaskDao
import com.lovelessgeek.housemanager.data.db.TaskEntity

class RepositoryImpl(db: LocalDatabase) : Repository {

    private val taskDao: TaskDao = db.getTaskDao()

    override fun loadAllTasks(): List<TaskEntity> {
        return taskDao.loadAllTasks()
    }

    override fun addNewTask(task: TaskEntity) {
        taskDao.insert(task)
    }

    override fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }
}