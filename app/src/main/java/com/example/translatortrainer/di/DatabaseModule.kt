package com.example.translatortrainer.di

import android.app.Application
import com.example.translatortrainer.room.HistoryDAO
import com.example.translatortrainer.room.HistoryDatabase
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

