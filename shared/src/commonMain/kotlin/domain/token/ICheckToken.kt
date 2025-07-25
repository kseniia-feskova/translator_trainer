package domain.token

import data.model.base.Result

interface ICheckToken {
    suspend fun <T> safeApiCallWithRefresh(
        call: suspend () -> Result<T>,
        onTokenExpired: suspend () -> Boolean
    ): Result<T>
}