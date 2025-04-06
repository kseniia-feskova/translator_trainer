package com.domain.usecase.auth

import translator.data.prefs.IDataStoreManager
import translator.data.repository.auth.IAuthRepository
import com.presentation.usecases.auth.ILoginUseCase
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class LoginUseCase(private val repo: IAuthRepository, private val dataStore: IDataStoreManager) :
    ILoginUseCase {

    override suspend fun invoke(email: String, username: String, password: String): Result<UUID> {
        val response = repo.login(email, username, password)
        return if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
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

    override fun listenUserId(): Flow<UUID?> {
        return dataStore.listenUserId()
    }
}