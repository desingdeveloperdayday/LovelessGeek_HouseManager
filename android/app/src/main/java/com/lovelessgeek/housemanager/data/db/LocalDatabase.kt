package com.lovelessgeek.housemanager.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lovelessgeek.housemanager.data.db.util.DateConverter
import com.lovelessgeek.housemanager.data.db.util.EnumConverter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [TaskEntity::class], version = 5, exportSchema = false)
@TypeConverters(DateConverter::class, EnumConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao

    companion object {
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            return INSTANCE ?: run {
                synchronized(LocalDatabase::class) {
                    buildDatabase(
                        context
                    )
                        .apply { INSTANCE = this }
                }
            }
        }

        private fun buildDatabase(context: Context): LocalDatabase {
            return Room
                .databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "HouseManager.db"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        getInstance(context).getTaskDao().let { dao ->
                            GlobalScope.launch {
                                listOf(createMockData(), createCompletedMockData())
                                    .flatten()
                                    .map(TaskMapper::toEntity)
                                    .forEach {
                                        dao.insertTask(it)
                                    }
                            }
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}