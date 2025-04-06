package com.domain.usecase.auth.verify

import translator.data.prefs.IDataStoreManager
import translator.data.repository.auth.IAuthRepository
import com.presentation.usecases.auth.verify.IVerifyCodeUseCase
import java.util.UUID

class VerifyCodeUseCase(
    private val authRepository: IAuthRepository,
    private val dataStore: IDataStoreManager,
) : IVerifyCodeUseCase {
    override suspend fun invoke(code: String): Result<UUID> {
        val email = dataStore.getEmail() ?: return Result.failure(Exception("User does not exist"))
        val response = authRepository.verify(email, code)
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else {
            val userId = response.data?.uuid
            if (userId != null) {
                dataStore.resetGuestMode()
                dataStore.saveUserId(userId)
                Result.success(userId)
            } else {
                val errorMsg = response.data?.error
                Result.failure(Exception(errorMsg ?: "User does not exist"))
            }
        }
    }
}