package com.data.repository.auth

import android.util.Log
import com.data.api.ApiService
import com.data.api.Result
import com.data.model.auth.AuthRequest
import com.data.model.auth.AuthResponse
import com.data.model.auth.RefreshTokenRequest
import com.data.prefs.ITokenStorage
import com.data.prefs.TokenStorage.Companion.ACCESS_TOKEN
import com.data.prefs.TokenStorage.Companion.REFRESH_TOKEN
import com.data.safeCall

class AuthRepository(
    private val service: ApiService,
    private val tokenStorage: ITokenStorage
) : IAuthRepository {

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): Result<AuthResponse> {
        return safeCall({
            service.register(AuthRequest(email = email, username = username, password = password))
        }, onSuccess = { body ->
            tokenStorage.saveToken(ACCESS_TOKEN, body.accessToken)
            tokenStorage.saveToken(REFRESH_TOKEN, body.refreshToken)
            if (body.uuid == null) {
                Result(errorMsg = "Empty uuid")
            } else {
                tokenStorage.saveUserId(body.uuid)
                Log.e("register", "uuid = ${body.uuid}")
                Result(data = body)
            }
        })
    }

    override suspend fun login(
        email: String,
        username: String,
        password: String
    ): Result<AuthResponse> {
        return safeCall({
            service.login(AuthRequest(email = email, username = username, password = password))
        }, onSuccess = { body ->
            tokenStorage.saveToken(ACCESS_TOKEN, body.accessToken)
            tokenStorage.saveToken(REFRESH_TOKEN, body.refreshToken)
            if (body.uuid == null) {
                Result(errorMsg = "Empty user id")
            } else {
                tokenStorage.saveUserId(body.uuid)
                Result(data = body)
            }
        })
    }

    override suspend fun refreshToken(token: String): Result<AuthResponse> {
        return safeCall({
            service.refreshToken(RefreshTokenRequest(token))
        })
    }

    override suspend fun logout() {
        tokenStorage.clearToken(ACCESS_TOKEN)
        tokenStorage.clearToken(REFRESH_TOKEN)
        tokenStorage.clearUserId()
    }
}
