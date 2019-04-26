package com.lovelessgeek.housemanager.shared

import java.time.LocalDateTime

data class User(
    val id: String,
    val username: String,
    val tasks: List<Task> = listOf()
)

abstract class Task(
    open val type: TaskType,
    open val id: String,
    open val name: String
)

enum class TaskType {
    INSTANT
}

data class InstantTask(
    override val type: TaskType = TaskType.INSTANT,
    override val id: String,
    override val name: String,
    val time: LocalDateTime
) : Task(type, id, name)

// TODO: replace with db call
val task1 = InstantTask(id = "1", name = "청소하기", time = LocalDateTime.now().plusHours(2L))
val task2 = InstantTask(id = "2", name = "세탁하기", time = LocalDateTime.now().plusDays(1L))
val tasks: List<Task> = listOf(task1, task2)

val user1 = User("1", "tura", listOf(task1))
val user2 = User("2", "geonho")
val user3 = User("3", "alan", listOf(task2))
val users = listOf(user1, user2, user3)