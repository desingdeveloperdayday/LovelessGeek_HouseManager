package com.lovelessgeek.housemanager.data

data class Task @JvmOverloads constructor(
    var type: TaskType  = TaskType.NOT_CATEGORIZED,
    var title: String   = "Default Task Job",
    var startsAt: Long  = 0
)

enum class TaskType {
    NOT_CATEGORIZED
}