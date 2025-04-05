package com.data.repository.auth

import com.data.api.ApiService
import com.data.model.auth.AuthRequest
import com.data.model.auth.AuthResponse
import com.data.model.auth.RefreshTokenRequest
import com.data.model.base.Result
import com.data.prefs.ITokenStorage
import com.data.prefs.TokenStorage.Companion.ACCESS_TOKEN
import com.data.prefs.TokenStorage.Companion.REFRESH_TOKEN
import com.data.room.AppDao
import com.data.safeCall

class AuthRepository(
    private val service: ApiService,
    private val tokenStorage: ITokenStorage,
    private val appDao: AppDao
) : IAuthRepository {

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): Result<AuthResponse> {
        val request = AuthRequest(
            email = email,
            username = username,
            password = password
        )
        return safeCall(
            request = { service.register(request) },
            onSuccess = { body ->
                tokenStorage.saveToken(ACCESS_TOKEN, body.accessToken)
                tokenStorage.saveToken(REFRESH_TOKEN, body.refreshToken)
                if (body.uuid == null) {
                    Result(errorMsg = "Empty uuid")
                } else {
                    Result(data = body)
                }
            })
    }

    override suspend fun login(
        email: String,
        username: String,
        password: String
    ): Result<AuthResponse> {
        val request = AuthRequest(email = email, username = username, password = password)
        return safeCall({
            service.login(request)
        }, onSuccess = { body ->
            tokenStorage.saveToken(ACCESS_TOKEN, body.accessToken)
            tokenStorage.saveToken(REFRESH_TOKEN, body.refreshToken)
            if (body.uuid == null) {
                Result(errorMsg = "Empty user id")
            } else {
                Result(data = body)
            }
        })
    }

    override suspend fun refreshToken(token: String): Result<AuthResponse> {
        return safeCall({ service.refreshToken(RefreshTokenRequest(token)) })
    }


    override suspend fun verify(email: String, code: String): Result<AuthResponse> {
        return safeCall({
            service.verifyCode(email, code)
        }, onSuccess = { body ->
            tokenStorage.saveToken(ACCESS_TOKEN, body.accessToken)
            tokenStorage.saveToken(REFRESH_TOKEN, body.refreshToken)
            if (body.uuid == null) {
                Result(errorMsg = "Empty user id")
            } else {
                Result(data = body)
            }
        })
    }

    override suspend fun resendCode(email: String): Result<AuthResponse> {
        return safeCall({
            service.resendCode(email)
        }, onSuccess = { body ->
            if (body.uuid == null) {
                Result(errorMsg = "Empty user id")
            } else {
                Result(data = body)
            }
        })
    }

    override suspend fun clearCode(email: String) {
        safeCall({ service.clearCode(email) }, onSuccess = {})
    }

    override suspend fun logout() {
        tokenStorage.clearToken(ACCESS_TOKEN)
        tokenStorage.clearToken(REFRESH_TOKEN)
        clearDatabase()
    }

    private suspend fun clearDatabase() {
        appDao.clearUsers()
        appDao.clearSets()
        appDao.clearWords()
    }
}
