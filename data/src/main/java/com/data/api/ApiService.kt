package com.data.api

import com.data.model.course.CourseEntity
import com.data.model.UserEntity
import com.data.model.auth.AuthRequest
import com.data.model.auth.AuthResponse
import com.data.model.auth.LoginRequest
import com.data.model.auth.RefreshTokenRequest
import com.data.model.sets.AddSetRequest
import com.data.model.sets.SetResponse
import com.data.model.words.AddWordRequest
import com.data.model.words.WordResponse
import com.data.model.words.get.bytranslate.WordByOriginalRequest
import com.data.model.words.get.bytranslate.WordByTranslatedRequest
import com.data.model.sets.get.all.GetAllRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {

    //auth region

    @POST("auth/register")
    suspend fun register(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body authRequest: LoginRequest): Response<AuthResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body tokenRequest: RefreshTokenRequest): Response<AuthResponse>

    //end of region

    //user region

    @GET("user/{uuid}")
    suspend fun getUserById(@Path("uuid") uuid: String): Response<UserEntity>

    //end of region

    //course region

    @GET("course/{uuid}")
    suspend fun getCourseById(@Path("uuid") uuid: String): Response<CourseEntity>

    //end of region

    //set region

    @GET("sets/get_all")
    suspend fun getAllSets(@Body request: GetAllRequest): Response<List<SetResponse>>

    @POST("sets/add")
    suspend fun saveSet(@Body addWordRequest: AddSetRequest): Response<SetResponse>

    //end of region

    //words region

    @POST("words/add")
    suspend fun saveWorld(@Body addWordRequest: AddWordRequest): Response<WordResponse>

    @POST("words/check_by_translated")
    suspend fun getWordByTranslated(@Body request: WordByTranslatedRequest): Response<WordResponse>

    @POST("words/check_by_original")
    suspend fun getWordByOriginal(@Body request: WordByOriginalRequest): Response<WordResponse>

    //end of region

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

