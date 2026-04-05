package di

import data.api.ApiService
import data.api.TranslateService
import data.api.provideHttpClient
import data.prefs.ITokenStorage
import data.prefs.TokenStorage
import org.koin.dsl.module
import presentation.TextRecognizer

val networkModule = module {

    single<ITokenStorage> { TokenStorage(get()) }

    single {
        provideHttpClient(get())
    }

    single { ApiService(get()) }

    //TODO: Remove?
    single { TranslateService(get()) }

    single<TextRecognizer> { TextRecognizer() }
}