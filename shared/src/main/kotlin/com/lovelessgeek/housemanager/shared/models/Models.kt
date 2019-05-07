package com.lovelessgeek.housemanager.shared.models

import java.util.*

data class User(
    val id: String,
    val username: String,
    val tasks: List<Task> = listOf()
)

abstract class Task(
    open val type: String,
    open val id: String,
    open val name: String
)

/*
enum class TaskType(var type: String? = null) {
    INSTANT(null)
}
*/
class TaskType {
    companion object {
        const val INSTANT = "0x0000"
    }
}

data class InstantTask(
    override val type: String = TaskType.INSTANT,
    override val id: String,
    override val name: String,
    val time: Date
) : Task(type, id, name)