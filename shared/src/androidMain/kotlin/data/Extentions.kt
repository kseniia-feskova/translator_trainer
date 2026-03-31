package data

import android.util.Log
import data.api.UnauthorizedException
import data.model.base.Result
import domain.token.TokenRefresher.Companion.ERROR_TOKEN_EXPIRED
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText

suspend fun <T> safeCall(
    request: suspend () -> T,
    onSuccess: (T) -> Unit = {},
    onRefresh: () -> Unit = {}
): Result<T> {
    return try {
        val response: T? = request()
        if (response != null) {
            onSuccess(response)
            Result(data = response)
        } else {
            Result(errorMsg = "Empty data")
        }
    } catch (_: UnauthorizedException) {
        onRefresh()
        return Result(errorMsg = ERROR_TOKEN_EXPIRED)
    } catch (e: ClientRequestException) {
        Result(errorMsg = e.response.bodyAsText())
    } catch (e: Exception) {
        Log.e("Extentions", "safeCall exception = ${e.message}")
        Result(errorMsg = e.message.toString())
    }
}

//fun <T> extractErrorMessage(response: Response<T>): String? {
//    val errorBody = response.errorBody()
//    return if (errorBody != null) {
//        try {
//            val gson = Gson()
//            val errorResponse = gson.fromJson(errorBody.string(), ErrorResponse::class.java)
//            errorResponse.details.message
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null // Возвращаем null, если произошла ошибка парсинга
//        }
//    } else {
//        null // Возвращаем null, если errorBody отсутствует
//    }
//}
