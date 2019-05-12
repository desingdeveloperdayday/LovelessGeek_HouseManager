package com.lovelessgeek.housemanager.data

import android.content.Context
import com.lovelessgeek.housemanager.data.db.LocalDatabase

object Provider {
    fun provideRepository(context: Context): Repository {
        return RepositoryImpl(LocalDatabase.getInstance(context))
    }
}