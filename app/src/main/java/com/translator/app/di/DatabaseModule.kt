package com.translator.app.di

import android.content.Context
import data.room.AppDao
import data.room.AppDatabase
import data.room.SetsDao
import data.room.WordDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    fun provideDatabase(application: Context): AppDatabase {
        return AppDatabase.getDatabase(application)
    }

    fun provideWordsDao(database: AppDatabase): WordDao {
        return database.wordDao()
    }

    fun provideSetsDao(database: AppDatabase): SetsDao {
        return database.setsDao()
    }

    fun provideAppDao(database: AppDatabase): AppDao {
        return database.appDao()
    }

    single { provideDatabase(androidContext()) }
    single { provideWordsDao(get()) }
    single { provideSetsDao(get()) }
    single { provideAppDao(get()) }
}

