package com.presentation.usecases.auth

import java.util.UUID

interface IRegisterUseCase {
    suspend fun invoke(email: String, username: String, password: String): Result<UUID?>
}