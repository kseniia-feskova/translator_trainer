package com.domain.usecase.auth

import com.data.repository.auth.IAuthRepository
import com.presentation.usecases.auth.IRegisterUseCase
import com.presentation.utils.Language
import java.util.UUID

class RegisterUseCase(private val repo: IAuthRepository) : IRegisterUseCase {
    override suspend fun invoke(
        email: String,
        username: String,
        password: String,
        originalLanguage: Language,
        translateLanguage: Language
    ): Result<UUID?> {
        val response = repo.register(email, username, password, originalLanguage.code, translateLanguage.code)
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else Result.success(response.data?.uuid)
    }
}
