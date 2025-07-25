package usecase.auth

import com.presentation.usecases.auth.ILogoutUseCase
import data.prefs.IDataStoreManager
import data.repository.IAuthRepository
import domain.cache.ISetsCacheProvider

class LogoutUseCase(
    private val cache: ISetsCacheProvider,
    private val repo: IAuthRepository,
    private val dataStore: IDataStoreManager
) : ILogoutUseCase {

    override suspend fun invoke() {
        dataStore.saveUserId(null)
        dataStore.resetGuestMode()
        cache.clear()
        repo.logout()
    }

}