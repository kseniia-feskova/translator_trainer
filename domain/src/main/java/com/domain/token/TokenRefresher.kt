package com.domain.token

import android.util.Log
import translator.data.ERROR_TOKEN_EXPIRED
import translator.data.prefs.IDataStoreManager
import translator.data.prefs.ITokenStorage
import translator.data.prefs.TokenStorage.Companion.ACCESS_TOKEN
import translator.data.prefs.TokenStorage.Companion.REFRESH_TOKEN
import translator.data.repository.auth.IAuthRepository
import com.presentation.cache.ISetsCacheProvider

class TokenRefresher(
    private val authRepo: IAuthRepository,
    private val tokenStorage: ITokenStorage,
    private val cache: ISetsCacheProvider,
    private val prefs: IDataStoreManager,
) : ITokenRefresher {

    override suspend fun refreshToken(): Boolean {
        val refreshToken = tokenStorage.getToken(REFRESH_TOKEN)
        if (refreshToken != null) {
            tokenStorage.clearToken(ACCESS_TOKEN)
            val response = authRepo.refreshToken(refreshToken)
            val body = response.data
            if (body != null) {
                Log.e("refreshToken", "Token updated")
                tokenStorage.saveToken(ACCESS_TOKEN, body.accessToken)
                tokenStorage.saveToken(REFRESH_TOKEN, body.refreshToken)
                return true
            } else {
                if (response.errorMsg == ERROR_TOKEN_EXPIRED) {
                    Log.e("refreshToken", "Code = 401")
                    authRepo.logout()
                    cache.addSets(null)
                    prefs.saveUserId(null)
                } else {
                    Log.e("TokenRefresher", "Can not refresh token. Error msg = ${response.errorMsg}")
                }
                return false
            }
        } else {
            Log.e("refreshToken", "Token is null(")
            authRepo.logout()
            cache.addSets(null)
            prefs.saveUserId(null)
            return false
        }
    }
}