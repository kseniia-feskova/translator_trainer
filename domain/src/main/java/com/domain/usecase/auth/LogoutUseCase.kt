package com.domain.usecase.auth

import com.data.prefs.IDataStoreManager
import com.data.repository.auth.IAuthRepository
import com.presentation.cache.ISetsCacheProvider
import com.presentation.usecases.auth.ILogoutUseCase

class LogoutUseCase(
    private val cache: ISetsCacheProvider,
    private val repo: IAuthRepository,
    private val dataStore: IDataStoreManager
) : ILogoutUseCase {
    override suspend fun invoke() {
        dataStore.saveUserId(null)
        dataStore.resetGuestMode()
        cache.addSets(null)
        repo.logout()
    }
}