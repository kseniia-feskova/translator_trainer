package com.data.di

import android.app.Application
import com.data.room.HistoryDAO
import com.data.room.HistoryDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val databaseModule = module {

    fun provideDatabase(application: Application): HistoryDatabase {
        return HistoryDatabase.getInstance(application)
    }

    fun provideDao(database: HistoryDatabase): HistoryDAO {
        return database.historyDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
}

