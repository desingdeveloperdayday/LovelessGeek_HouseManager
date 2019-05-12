package com.lovelessgeek.housemanager.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query

@Dao
abstract class TaskDao :
    BaseDao<TaskEntity> {

    @Delete
    abstract fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM task")
    abstract fun loadAllTasks(): List<TaskEntity>

    //@Query("SELECT * FROM task WHERE time > :time")
    //abstract fun loadTasksAfter(time: Date): List<TaskEntity>
}