package com.data.api

import com.data.model.UserEntity
import com.data.model.auth.AuthRequest
import com.data.model.auth.AuthResponse
import com.data.model.auth.RefreshTokenRequest
import com.data.model.course.CourseEntity
import com.data.model.course.add.AddCourseRequest
import com.data.model.course.get.GetAllCoursesRequest
import com.data.model.sets.AddSetRequest
import com.data.model.sets.SetResponse
import com.data.model.sets.get.all.GetAllRequest
import com.data.model.words.WordResponse
import com.data.model.words.add.AddWordRequest
import com.data.model.words.get.bytranslate.WordByOriginalRequest
import com.data.model.words.get.bytranslate.WordByTranslatedRequest
import com.data.model.words.update.UpdateWordStatusRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface ApiService {

    //auth region

    @POST("auth/register")
    suspend fun register(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body tokenRequest: RefreshTokenRequest): Response<AuthResponse>

    @FormUrlEncoded
    @POST("auth/verify")
    suspend fun verifyCode(
        @Field("email") email: String,
        @Field("code") code: String
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST("auth/verify/resend_code")
    suspend fun resendCode(
        @Field("email") email: String,
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST("auth/verify/clear_code")
    suspend fun clearCode(
        @Field("email") email: String,
    ): Response<Unit>

    //end of region

    //user region

    @GET("user/{uuid}")
    suspend fun getUserById(@Path("uuid") uuid: String): Response<UserEntity>

    //end of region

    //course region

    @GET("course/{uuid}")
    suspend fun getCourseById(@Path("uuid") uuid: String): Response<CourseEntity>

    @POST("course/get_for_user")
    suspend fun getAllCourses(@Body request: GetAllCoursesRequest): Response<List<CourseEntity>>

    @POST("course/add")
    suspend fun addCourse(@Body request: AddCourseRequest): Response<CourseEntity>


    //end of region

    //set region

    @POST("sets/get_all")
    suspend fun getAllSets(@Body request: GetAllRequest): Response<List<SetResponse>>

    @POST("sets/add")
    suspend fun saveSet(@Body addWordRequest: AddSetRequest): Response<SetResponse>

    //end of region

    //words region

    @POST("words/add")
    suspend fun saveWorld(@Body addWordRequest: AddWordRequest): Response<WordResponse>

    @POST("words/check_by_translated")
    suspend fun getWordByTranslated(@Body request: WordByTranslatedRequest): Response<WordResponse>

    @POST("words/check_by_origin")
    suspend fun getWordByOriginal(@Body request: WordByOriginalRequest): Response<WordResponse>

    @GET("words/get_by_set/{setId}")
    suspend fun getWordsBySet(@Path("setId") setId: UUID): Response<List<WordResponse>>

    @PATCH("words/update_status/{id}")
    suspend fun updateStatus(
        @Path("id") wordId: UUID,
        @Body updateStatusRequest: UpdateWordStatusRequest
    ): Response<WordResponse>

    @DELETE("words/delete/{id}")
    suspend fun deleteWord(@Path("id") wordId: UUID): Response<Void>

    //end of region
}
