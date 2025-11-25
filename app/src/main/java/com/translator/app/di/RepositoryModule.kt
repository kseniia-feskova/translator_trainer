package com.translator.app.di

import data.repos.AuthRepository
import data.repos.CourseRepository
import data.repos.SetApiRepository
import data.repos.SetDaoRepository
import data.repos.UserRepository
import data.repos.WordApiRepository
import data.repos.WordDaoRepository
import data.repository.ICourseRepository
import data.repository.set.ISetApiRepository
import data.repository.set.ISetDaoRepository
import data.repository.word.IWordDaoRepository
import data.repository.word.IWordApiRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    singleOf(::WordDaoRepository) bind IWordDaoRepository::class

    singleOf(::WordApiRepository) bind IWordApiRepository::class

    singleOf(::SetDaoRepository) bind ISetDaoRepository::class

    singleOf(::SetApiRepository) bind ISetApiRepository::class

    singleOf(::AuthRepository) bind data.repository.IAuthRepository::class

    singleOf(::UserRepository) bind data.repository.IUserRepository::class

    singleOf(::CourseRepository) bind ICourseRepository::class

}

