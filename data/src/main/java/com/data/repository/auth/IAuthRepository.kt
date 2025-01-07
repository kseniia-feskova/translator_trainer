package com.data.repository.auth

import com.data.api.AuthResponse
import com.data.api.Result

interface IAuthRepository {
    suspend fun register(email: String, password: String): Result<AuthResponse>
    suspend fun login(email: String, password: String): Result<AuthResponse>
    suspend fun refreshToken(token: String): Result<AuthResponse>
    suspend fun logout()
}
