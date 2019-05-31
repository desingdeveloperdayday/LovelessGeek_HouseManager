package com.lovelessgeek.housemanager.data.db

import com.lovelessgeek.housemanager.shared.models.Task

object TaskMapper : Mapper<Task, TaskEntity> {
    override fun toEntity(item: Task): TaskEntity {
        return TaskEntity(
            id = item.id,
            name = item.name,
            category = item.category,
            time = item.time,
            isComplete = item.isComplete,
            isRepeat = item.isRepeat,
            period = item.period,
            created = item.created,
            completed = item.completed
        )
    }

    // TODO: 추가
    override fun fromEntity(entity: TaskEntity): Task {
        return Task(
            id = entity.id,
            category = entity.category,
            name = entity.name,
            time = entity.time,
            isComplete = entity.isComplete,
            isRepeat = entity.isRepeat,
            period = entity.period,
            created = entity.created,
            completed = entity.completed
        )
    }
}