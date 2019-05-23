package com.lovelessgeek.housemanager.data.db

import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Task

object TaskMapper {
    fun toEntity(task: Task): TaskEntity {
        return TaskEntity(
            id = task.id,
            name = task.name,
            time = task.time
        )
    }

    // TODO: 추가
    fun fromEntity(task: TaskEntity): Task {
        return Task(
            id = task.id,
            category = Category.default,
            name = task.name,
            time = task.time
        )
    }
}