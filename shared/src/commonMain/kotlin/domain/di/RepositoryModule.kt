package domain.di

import data.repository.auth.AuthRepository
import data.repository.auth.IAuthRepository
import data.repository.course.CourseRepository
import data.repository.course.ICourseRepository
import data.repository.set.ISetApiRepository
import data.repository.set.SetApiRepository
import data.repository.user.IUserRepository
import data.repository.user.UserRepository
import data.repository.word.IWordApiRepository
import data.repository.word.WordApiRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    singleOf(::WordApiRepository) bind IWordApiRepository::class

    singleOf(::SetApiRepository) bind ISetApiRepository::class

    singleOf(::AuthRepository) bind IAuthRepository::class

    singleOf(::UserRepository) bind IUserRepository::class

    singleOf(::CourseRepository) bind ICourseRepository::class

}
