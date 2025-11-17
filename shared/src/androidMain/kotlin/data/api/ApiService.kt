package data.api

import data.model.UserEntity
import data.model.auth.AuthRequest
import data.model.auth.AuthResponse
import data.model.auth.FirebaseAuthRequest
import data.model.auth.RefreshTokenRequest
import data.model.course.CourseEntity
import data.model.course.add.AddCourseRequest
import data.model.sets.AddSetRequest
import data.model.sets.SetResponse
import data.model.words.WordResponse
import data.model.words.add.AddWordRequest
import data.model.words.update.UpdateWordStatusRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface ApiService {

    //auth region

    @POST("auth/register")
    suspend fun register(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("auth/firebase")
    suspend fun loginWithFirebase(@Body firebaseAuthRequest: FirebaseAuthRequest): Response<AuthResponse>

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

    //TODO: investigate this and add to the documentation
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

    @GET("course/get")
    suspend fun getAllCourses(
        @Query("userId") userId: UUID
    ): Response<List<CourseEntity>>

    @POST("course/add")
    suspend fun addCourse(@Body request: AddCourseRequest): Response<CourseEntity>


    //end of region

    //set region

    @GET("sets/get")
    suspend fun getAllSets(@Query("courseId") courseId: UUID): Response<List<SetResponse>>

    @POST("sets/add")
    suspend fun saveSet(@Body addWordRequest: AddSetRequest): Response<SetResponse>

    //end of region

    //words region

    @POST("words/add")
    suspend fun saveWorld(@Body addWordRequest: AddWordRequest): Response<WordResponse>

    @GET("words/check_by_translated")
    suspend fun getWordByTranslated(
        @Query("courseId") courseId: UUID,
        @Query("translate") translate: String
    ): Response<WordResponse>

    @GET("words/check_by_origin")
    suspend fun getWordByOriginal(
        @Query("courseId") courseId: UUID,
        @Query("original") original: String
    ): Response<WordResponse>

    @GET("words/get_by_set/{setId}")
    suspend fun getWordsBySet(@Path("setId") setId: String): Response<List<WordResponse>>

    @PATCH("words/update_status/{id}")
    suspend fun updateStatus(
        @Path("id") wordId: String,
        @Body updateStatusRequest: UpdateWordStatusRequest
    ): Response<WordResponse>

    @DELETE("words/delete/{id}")
    suspend fun deleteWord(@Path("id") wordId: String): Response<Void>

    //end of region
}
