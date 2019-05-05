package com.lovelessgeek.housemanager.data

import java.time.LocalDateTime

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
    val time: Long//LocalDateTime
) : Task(type, id, name)