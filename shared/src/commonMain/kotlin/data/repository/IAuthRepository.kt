package data.repository

import data.model.auth.AuthResponse
import data.model.base.Result

interface IAuthRepository {
    suspend fun register(email: String, username: String, password: String): Result<AuthResponse>
    suspend fun login(email: String, username: String, password: String): Result<AuthResponse>
    suspend fun refreshToken(token: String): Result<AuthResponse>
    suspend fun logout()

    suspend fun verify(email: String, code: String): Result<AuthResponse>
    suspend fun resendCode(email: String): Result<AuthResponse>
    suspend fun clearCode(email: String)
    suspend fun registerWithFirebase(
        uuid: String? = null,
        email: String? = null,
        displayName: String? = null,
        photo: String? = null,
        phone: String? = null
    ): Result<AuthResponse>
}
