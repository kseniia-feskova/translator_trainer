package com.domain.token

import android.util.Log
import com.data.ERROR_TOKEN_EXPIRED
import com.data.prefs.ITokenStorage
import com.data.prefs.TokenStorage.Companion.ACCESS_TOKEN
import com.data.prefs.TokenStorage.Companion.REFRESH_TOKEN
import com.data.repository.auth.IAuthRepository
import com.presentation.data.IDataStoreManager

class TokenRefresher(
    private val authRepo: IAuthRepository,
    private val tokenStorage: ITokenStorage,
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
                //TODO: test expired refresh token
                if (response.errorMsg == ERROR_TOKEN_EXPIRED) {
                    Log.e("refreshToken", "Code = 401")
                    authRepo.logout()
                    prefs.saveUserId(null)
                } else {
                    Log.e("TokenRefresher", "Can not refresh token. Error msg = ${response.errorMsg}")
                }
                return false
            }
        } else {
            Log.e("refreshToken", "Token is null(")
            authRepo.logout()
            prefs.saveUserId(null)
            return false
        }
    }
}