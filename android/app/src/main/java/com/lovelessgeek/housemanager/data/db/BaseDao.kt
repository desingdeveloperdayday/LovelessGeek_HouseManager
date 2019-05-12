package com.lovelessgeek.housemanager.data.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg obj: T)
}