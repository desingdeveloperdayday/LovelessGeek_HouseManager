package com.lovelessgeek.housemanager.base

import com.lovelessgeek.housemanager.data.Repository
import com.lovelessgeek.housemanager.data.RepositoryImpl
import com.lovelessgeek.housemanager.data.db.LocalDatabase
import com.lovelessgeek.housemanager.ui.main.MainViewModel
import com.lovelessgeek.housemanager.ui.main.notification.NotificationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { LocalDatabase.getInstance(androidContext()) }
    single<Repository> { RepositoryImpl(get()) }
    viewModel { MainViewModel() }
    viewModel { NotificationViewModel(get()) }
}
