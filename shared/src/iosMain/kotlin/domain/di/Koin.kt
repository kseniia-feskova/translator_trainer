package domain.di

import data.prefs.ILocalDatabase
import data.prefs.ITokenStorage
import domain.usecases.auth.ILoginUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.dsl.module

@Suppress("unused")
fun initKoin(
    tokenStorage: ITokenStorage,
    localDataBase: ILocalDatabase
) {
    startKoin { getAllModules(listOf(
        module{ single<ITokenStorage> { tokenStorage } },
        module{ single<ILocalDatabase> { localDataBase } }
    )) }
}

object KoinHelper: KoinComponent {
    fun getLoginUseCase(): ILoginUseCase = get()
}