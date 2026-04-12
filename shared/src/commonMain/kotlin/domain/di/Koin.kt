package domain.di

import data.prefs.IDataStoreManager
import data.prefs.ILocalDatabase
import data.prefs.ITokenStorage
import domain.usecases.auth.ILoginUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.auth.AuthInteractor

@Suppress("unused")
fun initKoin(
    tokenStorage: ITokenStorage,
    localDataBase: ILocalDatabase,
    dataStorageManager: IDataStoreManager
) {
    startKoin {
        modules(
            module {
                single<ITokenStorage> { tokenStorage }
                single<ILocalDatabase> { localDataBase }
                single<IDataStoreManager> { dataStorageManager }
            },
            networkModule, repositoryModule, useCaseModule, presentationModule
        )
    }
}

object KoinHelper : KoinComponent {

    fun getAuthInteractor(): AuthInteractor = get()

    fun getLoginUseCase(): ILoginUseCase = get()

}