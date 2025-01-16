package com.presentation.usecases

import com.presentation.model.UserUI
import java.util.UUID

interface IGetAccountUseCase {
    suspend fun invoke(userId: UUID): Result<UserUI>
}