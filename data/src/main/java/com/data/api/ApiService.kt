package com.data.api

import com.data.model.UserEntity
import com.data.model.auth.AuthRequest
import com.data.model.auth.AuthResponse
import com.data.model.auth.RefreshTokenRequest
import com.data.model.words.AddWordRequest
import com.data.model.words.AddWordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body tokenRequest: RefreshTokenRequest): Response<AuthResponse>

    @GET("user/{uuid}")
    suspend fun getUserById(@Path("uuid") uuid: String): Response<UserEntity>

    @POST("words/add")
    suspend fun saveWorld(@Body addWordRequest:AddWordRequest):Response<AddWordResponse>
}

data class ErrorDetails(
    val message: String
)

data class ErrorResponse(
    val status: String,
    val details: ErrorDetails
)

data class Result<T>(
    val data: T? = null,
    val errorMsg: String = ""
)

