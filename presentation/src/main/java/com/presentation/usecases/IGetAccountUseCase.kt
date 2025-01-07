package com.presentation.usecases

import com.presentation.model.User
import java.util.UUID

interface IGetAccountUseCase {
    suspend fun invoke(userId: UUID): Result<User>
}