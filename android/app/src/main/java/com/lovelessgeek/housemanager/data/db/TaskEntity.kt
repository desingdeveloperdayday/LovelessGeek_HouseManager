package com.lovelessgeek.housemanager.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lovelessgeek.housemanager.shared.models.Category
import java.util.Date
import java.util.UUID

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    var category: Category,
    var name: String,
    var time: Date = Date()
)// : Task(type, id, name)
