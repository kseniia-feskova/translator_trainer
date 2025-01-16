package com.data.repository.auth

import com.data.api.Result
import com.data.model.auth.AuthResponse

interface IAuthRepository {
    suspend fun register(
        email: String,
        username: String,
        password: String,
        originalLanguage: String,
        translateLanguage: String
    ): Result<AuthResponse>

    suspend fun login(email: String, username: String, password: String): Result<AuthResponse>
    suspend fun refreshToken(token: String): Result<AuthResponse>
    suspend fun logout()
}
