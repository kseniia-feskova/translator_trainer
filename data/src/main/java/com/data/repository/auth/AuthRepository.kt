package com.data.repository.auth

import android.util.Log
import com.data.api.ApiService
import com.data.api.AuthRequest
import com.data.api.AuthResponse
import com.data.api.ErrorResponse
import com.data.api.Result
import com.data.api.TokenRequest
import com.data.prefs.ITokenStorage
import com.data.prefs.TokenStorage.Companion.ACCESS_TOKEN
import com.data.prefs.TokenStorage.Companion.REFRESH_TOKEN
import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException

class AuthRepository(
    private val service: ApiService,
    private val tokenStorage: ITokenStorage
) : IAuthRepository {

    override suspend fun register(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = service.register(AuthRequest(email = email, password = password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    tokenStorage.saveToken(ACCESS_TOKEN, body.accessToken)
                    tokenStorage.saveToken(REFRESH_TOKEN, body.refreshToken)
                    if (body.uuid == null) {
                        Result(errorMsg = "Empty uuid")
                    } else {
                        tokenStorage.saveUserId(body.uuid)
                        Log.e("register", "uuid = ${body.uuid}")
                        Result(data = body)
                    }
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

    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = service.login(AuthRequest(email = email, password = password))
            Log.e("login", "response = ${response.isSuccessful}")
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    tokenStorage.saveToken(ACCESS_TOKEN, body.accessToken)
                    tokenStorage.saveToken(REFRESH_TOKEN, body.refreshToken)
                    if (body.uuid == null) {
                        Result(errorMsg = "Empty user id")
                    } else {
                        tokenStorage.saveUserId(body.uuid)
                        Result(data = body)
                    }
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

    override suspend fun refreshToken(token: String): Result<AuthResponse> {
        return try {
            val response = service.refreshToken(TokenRequest(token))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    tokenStorage.saveToken(ACCESS_TOKEN, body.accessToken)
                    tokenStorage.saveToken(REFRESH_TOKEN, body.refreshToken)
                    Result(data = body)
                } else {
                    Result(errorMsg = "Empty data")
                }
            } else {
                Result(errorMsg = response.errorBody()?.toString().toString())
            }
        } catch (e: Exception) {
            Result(errorMsg = e.message.toString())
        }
    }

    override suspend fun logout() {
        tokenStorage.clearToken(ACCESS_TOKEN)
        tokenStorage.clearToken(REFRESH_TOKEN)
        tokenStorage.clearUserId()
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