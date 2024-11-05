package com.data.di

import android.app.Application
import com.data.room.AppDatabase
import com.data.room.WordDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val databaseModule = module {

    fun provideDatabase(application: Application): AppDatabase {
        return AppDatabase.getDatabase(application)
    }

    fun provideDao(database: AppDatabase): WordDao {
        return database.wordDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
}

