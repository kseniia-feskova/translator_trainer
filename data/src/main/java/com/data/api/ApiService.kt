package com.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.UUID


interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("auth/refresh}")
    suspend fun refreshToken(@Body tokenRequest: TokenRequest): Response<AuthResponse>
}

data class ErrorDetails(
    val message: String
)

data class ErrorResponse(
    val status: String,
    val details: ErrorDetails
)

data class AuthRequest(
    val email: String,
    val password: String
)

data class TokenRequest(
    val token: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val uuid: UUID?
)

data class Result<T>(
    val data: T? = null,
    val errorMsg: String = ""
)

