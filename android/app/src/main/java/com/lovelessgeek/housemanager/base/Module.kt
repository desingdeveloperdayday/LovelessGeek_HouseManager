package com.lovelessgeek.housemanager.base

import com.lovelessgeek.housemanager.data.Repository
import com.lovelessgeek.housemanager.data.RepositoryImpl
import com.lovelessgeek.housemanager.data.db.LocalDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { LocalDatabase.getInstance(androidContext()) }
    single<Repository> { RepositoryImpl(get()) }
}
