package domain.di

import org.koin.dsl.module
import presentation.auth.AuthInteractor

val presentationModule = module{

    single { AuthInteractor(get(), get(), get(), get(), get(), get()) }

}