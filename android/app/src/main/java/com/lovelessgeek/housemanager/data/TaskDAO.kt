package com.lovelessgeek.housemanager.data
/*
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Date

@Dao
interface TaskDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTask(task: TaskEntity)

    @Delete
    fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM task")
    fun loadAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM task WHERE time > :time")
    fun loadTasksAfter(time: Date): List<TaskEntity>
}
*/