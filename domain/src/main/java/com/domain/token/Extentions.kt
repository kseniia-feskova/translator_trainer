package com.domain.token

import com.data.ERROR_TOKEN_EXPIRED
import com.data.api.Result

suspend fun <T> safeApiCallWithRefresh(
    call: suspend () -> Result<T>,
    onTokenExpired: suspend () -> Boolean
): Result<T> {
    val response = call()
    if (response.errorMsg == ERROR_TOKEN_EXPIRED) {
        val isTokenRefreshed = onTokenExpired()
        if (isTokenRefreshed) {
            return call()
        }
    }
    return response
}