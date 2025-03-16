package com.domain.usecase.auth

import com.data.prefs.IDataStoreManager
import com.data.repository.auth.IAuthRepository
import com.presentation.usecases.auth.IRegisterUseCase
import java.util.UUID

class RegisterUseCase(
    private val repo: IAuthRepository,
    private val dataStore: IDataStoreManager
) : IRegisterUseCase {
    override suspend fun invoke(
        email: String,
        username: String,
        password: String
    ): Result<UUID> {
        val response = repo.register(email, username, password)
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else {
            val userId = response.data?.uuid
            if (userId != null) {
                dataStore.saveUserId(userId)
                Result.success(userId)
            } else {
                Result.failure(Exception("User does not exist"))
            }
        }
    }
}
