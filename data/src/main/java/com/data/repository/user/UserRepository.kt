package com.data.repository.user

import com.data.api.ApiService
import com.data.api.ErrorResponse
import com.data.api.Result
import com.data.model.UserEntity
import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException
import java.util.UUID

class UserRepository(private val apiService: ApiService) : IUserRepository {
    override suspend fun getUserById(id: UUID): Result<UserEntity> {
        return saveCall(
            request = { apiService.getUserById(id.toString()) }
        )
    }
}


suspend fun <T> saveCall(
    request: suspend () -> Response<T>,
    onSuccess: (T) -> Unit = {}
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
            val errorResponse = extractErrorMessage(response)
            println("Error occurred: $errorResponse")
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