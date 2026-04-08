package domain.usecases.auth

import data.prefs.IDataStoreManager
import data.prefs.ILocalDatabase
import data.repository.auth.IAuthRepository
import domain.cache.ISetsCacheProvider

class LogoutUseCase(
    private val cache: ISetsCacheProvider,
    private val repo: IAuthRepository,
    private val dataStore: IDataStoreManager,
    private val localDatabase: ILocalDatabase
) : ILogoutUseCase {

    override suspend fun invoke() {
        dataStore.saveUserId(null)
        dataStore.resetGuestMode()
        cache.clear()
        localDatabase.clearDatabase()
        repo.logout()
    }

}