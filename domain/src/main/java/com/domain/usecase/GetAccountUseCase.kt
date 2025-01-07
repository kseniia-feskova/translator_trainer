package com.domain.usecase

import com.data.repository.user.IUserRepository
import com.domain.mapper.toUI
import com.presentation.model.User
import com.presentation.usecases.IGetAccountUseCase
import java.util.UUID

class GetAccountUseCase(
    private val repo: IUserRepository
) : IGetAccountUseCase {
    override suspend fun invoke(userId: UUID): Result<User> {
        val response = repo.getUserById(userId)
        val data = response.data
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data.toUI())
    }
}