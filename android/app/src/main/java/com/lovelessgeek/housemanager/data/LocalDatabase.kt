package com.lovelessgeek.housemanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lovelessgeek.housemanager.data.util.DateConverter

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class/*, TaskTypeConverter::class*/)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao

    companion object {
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase? {
            if (INSTANCE == null) {
                synchronized(LocalDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, LocalDatabase::class.java, "HouseManager.db")
                        .build()
                }
            }
            return INSTANCE
        }
    }
}