package com.example.translatortrainer.di

import com.example.translatortrainer.data.WordsRepository
import com.example.translatortrainer.room.HistoryDAO
import com.example.translatortrainer.utils.CustomTranslator
import org.koin.dsl.module

val repositoryModule = module {
    fun provideMainRepository(dao: HistoryDAO, translator: CustomTranslator): WordsRepository {
        return WordsRepository(dao, translator)
    }

    single { provideMainRepository(get(), get()) }
}