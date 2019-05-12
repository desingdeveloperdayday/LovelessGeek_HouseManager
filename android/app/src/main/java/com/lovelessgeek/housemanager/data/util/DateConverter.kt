package com.lovelessgeek.housemanager.data.util

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun toDate(date: Long) = Date(date)

    @TypeConverter
    fun fromDate(date: Date) = date.time
}