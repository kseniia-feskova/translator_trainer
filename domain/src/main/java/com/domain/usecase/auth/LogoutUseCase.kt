package com.domain.usecase.auth

import com.data.repository.auth.IAuthRepository
import com.presentation.cache.ISetsCacheProvider
import com.presentation.usecases.auth.ILogoutUseCase

class LogoutUseCase(
    private val cache: ISetsCacheProvider,
    private val repo: IAuthRepository
) : ILogoutUseCase {
    override suspend fun invoke() {
        cache.addSets(null)
        repo.logout()
    }
}