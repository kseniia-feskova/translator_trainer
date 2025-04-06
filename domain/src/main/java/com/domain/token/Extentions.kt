package com.domain.token

import translator.data.ERROR_TOKEN_EXPIRED
import translator.data.model.base.Result

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