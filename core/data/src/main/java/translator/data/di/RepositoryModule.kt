package translator.data.di

import translator.data.prefs.DataStoreManager
import translator.data.prefs.IDataStoreManager
import translator.data.repository.auth.AuthRepository
import translator.data.repository.auth.IAuthRepository
import translator.data.repository.course.CourseRepository
import translator.data.repository.course.ICourseRepository
import translator.data.repository.sets.ISetRepository
import translator.data.repository.sets.api.SetApiRepository
import translator.data.repository.sets.room.SetsDaoRepository
import translator.data.repository.translate.ITranslateRepository
import translator.data.repository.translate.TranslateRepository
import translator.data.repository.user.IUserRepository
import translator.data.repository.user.UserRepository
import translator.data.repository.words.api.IWordApiRepository
import translator.data.repository.words.IWordRepository
import translator.data.repository.words.api.WordApiRepository
import translator.data.repository.words.room.WordsDaoRepository
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

