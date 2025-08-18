package data

import com.google.gson.Gson
import data.model.base.ErrorResponse
import data.model.base.Result
import domain.token.TokenRefresher.Companion.ERROR_TOKEN_EXPIRED
import retrofit2.Response
import java.io.IOException

suspend fun <T> safeCall(
    request: suspend () -> Response<T>,
    onSuccess: (T) -> Unit = {},
    onRefresh: () -> Unit = {}
): Result<T> {
    return try {
        val response: Response<T> = request()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                onSuccess(body)
                Result(data = body)
            } else {
                Result(errorMsg = "Empty data")
            }
        } else {
            if (response.code() == 401) {
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

fun <T> extractErrorMessage(response: Response<T>): String? {
    val errorBody = response.errorBody()
    return if (errorBody != null) {
        try {
            val gson = Gson()
            val errorResponse = gson.fromJson(errorBody.string(), ErrorResponse::class.java)
            errorResponse.details.message
        } catch (e: IOException) {
            e.printStackTrace()
            null // Возвращаем null, если произошла ошибка парсинга
        }
    } else {
        null // Возвращаем null, если errorBody отсутствует
    }
}
