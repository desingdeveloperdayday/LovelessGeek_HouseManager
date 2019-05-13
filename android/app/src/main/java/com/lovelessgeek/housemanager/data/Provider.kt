package com.lovelessgeek.housemanager.data

import android.content.Context
import com.lovelessgeek.housemanager.data.db.LocalDatabase

object Provider {
    private var repository: Repository? = null

    fun provideRepository(context: Context): Repository {
        return repository ?: makeRepository(context)
            .also { repository = it }
    }

    private fun makeRepository(context: Context) =
        RepositoryImpl(LocalDatabase.getInstance(context.applicationContext))
}