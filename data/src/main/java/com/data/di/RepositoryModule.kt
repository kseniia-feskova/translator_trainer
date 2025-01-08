package com.data.di

import com.data.repository.auth.AuthRepository
import com.data.repository.auth.IAuthRepository
import com.data.repository.translate.ITranslateRepository
import com.data.repository.translate.TranslateRepository
import com.data.repository.user.IUserRepository
import com.data.repository.user.UserRepository
import com.data.repository.words.api.IWordsApiRepository
import com.data.repository.words.api.WordsApiRepository
import com.data.repository.words.room.IWordsDaoRepository
import com.data.repository.words.room.WordsDaoRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::TranslateRepository) bind ITranslateRepository::class

    singleOf(::WordsApiRepository) bind IWordsApiRepository::class

    singleOf(::WordsDaoRepository) bind IWordsDaoRepository::class

    singleOf(::AuthRepository) bind IAuthRepository::class

    singleOf(::UserRepository) bind IUserRepository::class
}

