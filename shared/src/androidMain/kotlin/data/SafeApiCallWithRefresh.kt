package data

import data.model.base.Result
import domain.token.ICheckToken
import domain.token.TokenRefresher.Companion.ERROR_TOKEN_EXPIRED

class CheckToken: ICheckToken {

    override suspend fun <T> safeApiCallWithRefresh(
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
}