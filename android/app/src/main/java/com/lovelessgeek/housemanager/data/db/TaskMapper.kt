package com.lovelessgeek.housemanager.data.db

import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Task

object TaskMapper : Mapper<Task, TaskEntity> {
    override fun toEntity(item: Task): TaskEntity {
        return TaskEntity(
            id = item.id,
            name = item.name,
            time = item.time
        )
    }

    // TODO: 추가
    override fun fromEntity(entity: TaskEntity): Task {
        return Task(
            id = entity.id,
            category = Category.default,
            name = entity.name,
            time = entity.time
        )
    }
}