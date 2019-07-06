package com.lovelessgeek.housemanager.data.db.util

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun toDate(date: Long?) = date?.let { Date(it) }

    @TypeConverter
    fun fromDate(date: Date?) = date?.time
}