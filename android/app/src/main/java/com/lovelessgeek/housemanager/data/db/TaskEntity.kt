package com.lovelessgeek.housemanager.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lovelessgeek.housemanager.shared.models.Category
import java.util.Date

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey val id: String = "TASK_ID",
    var category: Category = Category.Default,
    var name: String = "TASK_NAME",
    var time: Date = Date(0)
)// : Task(type, id, name)
