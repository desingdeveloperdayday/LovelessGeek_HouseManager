package com.lovelessgeek.housemanager.data

import com.lovelessgeek.housemanager.shared.models.Task
import com.lovelessgeek.housemanager.shared.models.User

interface Repository {
    suspend fun getUsers(): List<User>

    suspend fun getUserById(id: String): User?

    suspend fun getTasks(): List<Task>

    suspend fun getTaskById(id: String): Task?
}