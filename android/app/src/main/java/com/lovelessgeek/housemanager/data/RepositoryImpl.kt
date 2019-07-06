package com.lovelessgeek.housemanager.data

import com.lovelessgeek.housemanager.data.db.LocalDatabase
import com.lovelessgeek.housemanager.data.db.TaskDao
import com.lovelessgeek.housemanager.data.db.TaskMapper
import com.lovelessgeek.housemanager.data.db.createCompletedMockData
import com.lovelessgeek.housemanager.data.db.createMockData
import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Task

class RepositoryImpl(db: LocalDatabase) : Repository {
    private val taskDao: TaskDao = db.getTaskDao()

    override suspend fun loadAllTasks(): List<Task> {
        return taskDao.loadAllTasks().map(TaskMapper::fromEntity)
    }

    override suspend fun loadTodoTasks(): List<Task> {
        return taskDao.loadTodoTasks().map(TaskMapper::fromEntity)
    }

    override suspend fun loadCompletedTasks(): List<Task> {
        return taskDao.loadCompletedTasks().map(TaskMapper::fromEntity)
    }

    override suspend fun addNewTask(task: Task) {
        taskDao.insert(TaskMapper.toEntity(task))
    }

    override suspend fun deleteTasks(tasks: List<Task>) {
        taskDao.deleteTasks(tasks.map(TaskMapper::toEntity))
    }

    override suspend fun loadCategories(): List<Category> {
        return categoryList()
    }

    // TODO: Remove when network is done.
    private fun categoryList(): List<Category> {
        return Category.values().toList()
    }

    override suspend fun initialize() {
        taskDao.deleteAll()
        listOf(createMockData(), createCompletedMockData())
            .flatten()
            .map(TaskMapper::toEntity)
            .forEach {
                taskDao.insertTask(it)
            }
    }
}