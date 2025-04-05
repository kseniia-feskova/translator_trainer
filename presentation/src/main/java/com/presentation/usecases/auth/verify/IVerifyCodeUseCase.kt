package com.presentation.usecases.auth.verify

import java.util.UUID

interface IVerifyCodeUseCase {
    suspend fun invoke(code: String): Result<UUID>
}