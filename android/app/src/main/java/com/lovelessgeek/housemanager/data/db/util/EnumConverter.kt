package com.lovelessgeek.housemanager.data.db.util

import android.util.Log
import androidx.room.TypeConverter
import com.lovelessgeek.housemanager.shared.models.Category

class EnumConverter {
    @TypeConverter
    fun categoryToString(category: Category): String {
        return category.name
    }

    @TypeConverter
    fun stringToCategory(string: String): Category {
        return try {
            Category.valueOf(string)
        } catch (e: IllegalArgumentException) {
            Log.e("EnumConverter", "Converting $string to Category failed. Fall back to default.")
            Category.Default
        }
    }
}