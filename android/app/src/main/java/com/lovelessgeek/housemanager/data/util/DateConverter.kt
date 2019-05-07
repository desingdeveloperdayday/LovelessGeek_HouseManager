package com.lovelessgeek.housemanager.data.util

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    public fun toDate(date: Long) = Date(date)

    @TypeConverter
    public fun fromDate(date: Date) = date.time
}