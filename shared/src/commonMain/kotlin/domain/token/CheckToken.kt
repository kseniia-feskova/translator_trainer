package domain.token

import data.model.base.Result

class CheckToken: ICheckToken {

    override suspend fun <T> safeApiCallWithRefresh(
        call: suspend () -> Result<T>,
        onTokenExpired: suspend () -> Boolean
    ): Result<T> {
        val response = call()
        if (response.errorMsg == TokenRefresher.ERROR_TOKEN_EXPIRED) {
            val isTokenRefreshed = onTokenExpired()
            if (isTokenRefreshed) {
                return call()
            }
        }
        return response
    }
}