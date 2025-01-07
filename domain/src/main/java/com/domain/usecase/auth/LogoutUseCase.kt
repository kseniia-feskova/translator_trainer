package com.domain.usecase.auth

import com.data.repository.auth.IAuthRepository
import com.presentation.usecases.auth.ILogoutUseCase

class LogoutUseCase(
    private val repo: IAuthRepository
) : ILogoutUseCase {
    override suspend fun invoke() {
        repo.logout()
    }
}