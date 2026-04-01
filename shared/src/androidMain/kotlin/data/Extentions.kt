package data

import data.model.base.ErrorResponse
import data.model.base.Result
import domain.token.TokenRefresher.Companion.ERROR_TOKEN_EXPIRED
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import java.io.IOException


suspend inline fun <reified T> safeCall(
    request: suspend () -> HttpResponse,
    onSuccess: (T) -> Unit = {},
    onRefresh: () -> Unit = {}
): Result<T> {
    return try {
        val response: HttpResponse = request()
        if (response.status.isSuccess()) {
            val body = response.body<T>()
            if (body != null) {
                onSuccess(body)
                Result(data = body)
            } else {
                Result(errorMsg = "Empty data")
            }
        } else {
            if (response.status.value == 401) {
                onRefresh()
                return Result(errorMsg = ERROR_TOKEN_EXPIRED)
            }
            val errorResponse = extractErrorMessage(response)
            Result(errorMsg = errorResponse.toString())
        }
    } catch (e: Exception) {
        Result(errorMsg = e.message.toString())
    }
}

suspend fun extractErrorMessage(response: HttpResponse): String? {
    val errorBody = response.body<ErrorResponse?>()
    return if (errorBody != null) {
        try {
            errorBody.details.message
        } catch (e: IOException) {
            e.printStackTrace()
            null // Возвращаем null, если произошла ошибка парсинга
        }
    } else {
        null // Возвращаем null, если errorBody отсутствует
    }
}
