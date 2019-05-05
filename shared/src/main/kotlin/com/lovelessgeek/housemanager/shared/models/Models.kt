package com.lovelessgeek.housemanager.shared.models

import com.lovelessgeek.housemanager.shared.models.TaskType.INSTANT
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
    override val type: TaskType = INSTANT,
    override val id: String,
    override val name: String,
    val time: Long//LocalDateTime
) : Task(type, id, name)
