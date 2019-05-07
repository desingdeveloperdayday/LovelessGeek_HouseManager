package com.lovelessgeek.housemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lovelessgeek.housemanager.shared.models.TaskType
import java.util.Date

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey val id: String = "TASK_ID",
    var type: String         = TaskType.INSTANT,
    var name: String           = "TASK_NAME",
    var time: Date  = Date(0)
)// : Task(type, id, name)
