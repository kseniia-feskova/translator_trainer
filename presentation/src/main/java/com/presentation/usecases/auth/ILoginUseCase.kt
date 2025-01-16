package com.presentation.usecases.auth

import java.util.UUID

interface ILoginUseCase {
    suspend fun invoke(email: String, username: String, password: String): Result<UUID?>
}