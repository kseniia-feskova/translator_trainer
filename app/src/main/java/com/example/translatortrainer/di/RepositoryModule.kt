package com.example.translatortrainer.di

import com.data.data.WordsRepository
import com.data.room.HistoryDAO
import com.data.translate.CustomTranslator
import org.koin.dsl.module

val repositoryModule = module {
    fun provideMainRepository(dao: HistoryDAO, translator: CustomTranslator): WordsRepository {
        return WordsRepository(dao, translator)
    }

    single { provideMainRepository(get(), get()) }
}