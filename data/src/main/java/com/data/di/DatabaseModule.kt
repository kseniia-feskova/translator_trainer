package com.data.di

import android.app.Application
import com.data.room.AppDao
import com.data.room.AppDatabase
import com.data.room.SetsDao
import com.data.room.WordDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val databaseModule = module {

    fun provideDatabase(application: Application): AppDatabase {
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

    single { provideDatabase(androidApplication()) }
    single { provideWordsDao(get()) }
    single { provideSetsDao(get()) }
    single { provideAppDao(get()) }
}

