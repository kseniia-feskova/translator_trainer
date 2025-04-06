package translator.data.di

import android.app.Application
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import translator.data.room.AppDao
import translator.data.room.AppDatabase
import translator.data.room.SetsDao
import translator.data.room.WordDao


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

