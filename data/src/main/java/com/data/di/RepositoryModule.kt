package com.data.di

import com.data.prefs.DataStoreManager
import com.data.prefs.IDataStoreManager
import com.data.repository.auth.AuthRepository
import com.data.repository.auth.IAuthRepository
import com.data.repository.course.CourseRepository
import com.data.repository.course.ICourseRepository
import com.data.repository.sets.ISetRepository
import com.data.repository.sets.api.SetApiRepository
import com.data.repository.sets.room.SetsDaoRepository
import com.data.repository.translate.ITranslateRepository
import com.data.repository.translate.TranslateRepository
import com.data.repository.user.IUserRepository
import com.data.repository.user.UserRepository
import com.data.repository.words.api.IWordApiRepository
import com.data.repository.words.IWordRepository
import com.data.repository.words.api.WordApiRepository
import com.data.repository.words.room.WordsDaoRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    singleOf(::DataStoreManager) bind IDataStoreManager::class

    singleOf(::TranslateRepository) bind ITranslateRepository::class

    singleOf(::SetsDaoRepository) bind ISetRepository::class

    //TODO: made separated Api and Dao repositories and choose them in useCases
    factory<IWordRepository> {
        val isGuest = get<IDataStoreManager>().isGuestOnRuntime()
        if (isGuest == true) {
            WordsDaoRepository(get(), get())
        } else {
            WordApiRepository(get())
        }
    }

    singleOf(::WordApiRepository) bind IWordApiRepository::class

    //TODO: made separated Api and Dao repositories and choose them in useCases
    factory<ISetRepository> {
        val isGuest = get<IDataStoreManager>().isGuestOnRuntime()
        if (isGuest == true) {
            SetsDaoRepository(get(), get())
        } else {
            SetApiRepository(get())
        }
    }

    singleOf(::AuthRepository) bind IAuthRepository::class

    singleOf(::UserRepository) bind IUserRepository::class

    singleOf(::CourseRepository) bind ICourseRepository::class

}

