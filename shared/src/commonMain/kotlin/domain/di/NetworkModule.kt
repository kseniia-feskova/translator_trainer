package domain.di

import data.api.ApiService
import data.api.provideHttpClient
import org.koin.dsl.module

val networkModule = module {

    single {
        provideHttpClient(get())
    }

    single { ApiService(get()) }
}