package com.data.di

import com.data.api.TranslateService
import com.data.repository.translate.ITranslateRepository
import com.data.repository.translate.TranslateRepository
import com.data.repository.words.IWordsRepository
import com.data.repository.words.WordsRepository
import com.data.room.HistoryDAO
import org.koin.dsl.module

val repositoryModule = module {
    fun provideWordsRepository(dao: HistoryDAO): IWordsRepository {
        return WordsRepository(dao)
    }

    fun provideTranslateRepository(
        translator: TranslateService,
    ): ITranslateRepository {
        return TranslateRepository(translator)
    }

    single { provideWordsRepository(get()) }
    single { provideTranslateRepository(get()) }
}

val coreModule = module {}