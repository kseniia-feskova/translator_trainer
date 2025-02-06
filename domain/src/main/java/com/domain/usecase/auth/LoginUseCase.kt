package com.domain.usecase.auth

import com.data.repository.auth.IAuthRepository
import com.presentation.usecases.auth.ILoginUseCase
import java.util.UUID

class LoginUseCase(private val repo: IAuthRepository) : ILoginUseCase {

    override suspend fun invoke(email: String, username: String, password: String): Result<UUID?> {
        val response = repo.login(email, username, password)
        return if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else Result.success(response.data?.uuid)
    }

}