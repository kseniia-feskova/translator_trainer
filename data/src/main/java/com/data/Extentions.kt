package com.data

import android.util.Log
import com.data.api.ErrorResponse
import com.data.api.Result
import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException

const val ERROR_TOKEN_EXPIRED = "Need to refresh token"

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
                Log.e("saveCall", "Success, body !=null")
                onSuccess(body)
                Result(data = body)
            } else {
                Log.e("saveCall", "Success, but empty body")
                Result(errorMsg = "Empty data")
            }
        } else {
            Log.e("saveCall", "Error, response = ${response.message()}")
            if (response.code() == 401) {
                onRefresh()
                return Result(errorMsg = ERROR_TOKEN_EXPIRED)
            }
            val errorResponse = extractErrorMessage(response)
            Log.e("saveCall", "Error, response = $errorResponse")
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