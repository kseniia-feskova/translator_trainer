package com.domain.usecase.auth

import com.data.repository.auth.IAuthRepository
import com.presentation.usecases.auth.IRegisterUseCase
import java.util.UUID

class RegisterUseCase(private val repo: IAuthRepository) : IRegisterUseCase {
    override suspend fun invoke(email: String, password: String): Result<UUID?> {
        val response = repo.register(email, password)
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else Result.success(response.data?.uuid)
    }
}
