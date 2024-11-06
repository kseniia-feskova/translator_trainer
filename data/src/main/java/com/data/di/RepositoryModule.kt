package com.data.di

import com.data.mock.repo.IMockWordRepository
import com.data.mock.repo.MockWordRepository
import com.data.repository.translate.ITranslateRepository
import com.data.repository.translate.TranslateRepository
import com.data.repository.words.IWordsRepository
import com.data.repository.words.WordsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::TranslateRepository) bind ITranslateRepository::class

    singleOf(::MockWordRepository) bind IMockWordRepository::class

    singleOf(::WordsRepository) bind IWordsRepository::class
}

