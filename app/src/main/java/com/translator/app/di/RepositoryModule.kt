package com.translator.app.di

import data.repos.AuthRepository
import data.repos.CourseRepository
import data.repos.SetApiRepository
import data.repos.SetsDaoRepository
import data.repos.TranslateRepository
import data.repos.UserRepository
import data.repos.WordApiRepository
import data.repos.WordsDaoRepository
import data.repository.ICourseRepository
import data.repository.IWordApiRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    //singleOf(::DataStoreManager) bind data.prefs.IDataStoreManager::class

    singleOf(::TranslateRepository) bind data.repository.ITranslateRepository::class

    singleOf(::SetsDaoRepository) bind data.repository.ISetRepository::class

    //TODO: made separated Api and Dao repositories and choose them in useCases
    factory<data.repository.IWordRepository> {
        val isGuest = get<data.prefs.IDataStoreManager>().isGuestOnRuntime()
        if (isGuest == true) {
            WordsDaoRepository(get(), get())
        } else {
            WordApiRepository(get())
        }
    }

    singleOf(::WordApiRepository) bind IWordApiRepository::class

    //TODO: made separated Api and Dao repositories and choose them in useCases
    factory<data.repository.ISetRepository> {
        val isGuest = get<data.prefs.IDataStoreManager>().isGuestOnRuntime()
        if (isGuest == true) {
            SetsDaoRepository(get(), get())
        } else {
            SetApiRepository(get())
        }
    }

    singleOf(::AuthRepository) bind data.repository.IAuthRepository::class

    singleOf(::UserRepository) bind data.repository.IUserRepository::class

    singleOf(::CourseRepository) bind ICourseRepository::class

}

