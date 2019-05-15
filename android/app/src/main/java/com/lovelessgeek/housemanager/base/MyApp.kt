package com.lovelessgeek.housemanager.base

import android.app.Application
import com.google.firebase.FirebaseApp
import com.lovelessgeek.housemanager.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level.DEBUG
import org.koin.core.logger.Level.INFO

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        startKoin {
            androidLogger(logLevel())
            androidContext(this@MyApp)
            modules(appModule)
        }
    }

    private fun logLevel() = if (BuildConfig.DEBUG) DEBUG else INFO
}