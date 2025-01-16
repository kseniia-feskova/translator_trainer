package com.domain.usecase

import com.data.repository.user.IUserRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.model.UserUI
import com.presentation.usecases.IGetAccountUseCase
import java.util.UUID

class GetAccountUseCase(
    private val repo: IUserRepository,
    private val tokenRefresher: ITokenRefresher
) : IGetAccountUseCase {
    override suspend fun invoke(userId: UUID): Result<UserUI> {
        val response = safeApiCallWithRefresh(
            call = { repo.getUserById(userId) },
            onTokenExpired = { tokenRefresher.refreshToken() })
        val data = response.data
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data.toUI())
    }
}