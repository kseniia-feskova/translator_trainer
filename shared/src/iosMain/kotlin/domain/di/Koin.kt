package domain.di

import data.prefs.ILocalDatabase
import data.prefs.ITokenStorage
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
