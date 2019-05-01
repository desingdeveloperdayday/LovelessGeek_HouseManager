package com.lovelessgeek.housemanager.data

import com.lovelessgeek.housemanager.shared.models.Task
import com.lovelessgeek.housemanager.shared.models.User

class InMemoryRepository : Repository {
    override suspend fun getUsers(): List<User> {
        return users
    }

    override suspend fun getUserById(id: String): User? {
        return users.find { it.id == id }
    }

    override suspend fun getTasks(): List<Task> {
        return tasks
    }

    override suspend fun getTaskById(id: String): Task? {
        return tasks.find { it.id == id }
    }
}