package domain.di

import android.content.Context
import data.prefs.ILocalDatabase
import data.prefs.LocalDatabase
import data.repository.set.ISetDaoRepository
import data.repository.word.IWordDaoRepository
import data.room.AppDao
import data.room.AppDatabase
import data.room.SetsDao
import data.room.WordDao
import data.room.repos.SetDaoRepository
import data.room.repos.WordDaoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
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

    singleOf(::SetDaoRepository) bind ISetDaoRepository::class

    singleOf(::WordDaoRepository) bind IWordDaoRepository::class

    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    single<ILocalDatabase> { LocalDatabase(get(), get()) }

}

