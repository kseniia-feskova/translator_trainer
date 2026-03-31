package data.api

import data.model.auth.AuthRequest
import data.model.auth.AuthResponse
import data.model.auth.FirebaseAuthRequest
import data.model.auth.RefreshTokenRequest
import data.model.course.CourseEntity
import data.model.course.add.AddCourseRequest
import data.model.sets.AddSetRequest
import data.model.sets.SetResponse
import data.model.user.UserEntity
import data.model.words.WordResponse
import data.model.words.add.AddWordRequest
import data.model.words.update.UpdateWordStatusRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode

expect object AppConfig {
    val BASE_URL: String
}

class ApiService(
    private val client: HttpClient
) {
    //auth region
    suspend fun register(authRequest: AuthRequest): AuthResponse {
        return client.post("${AppConfig.BASE_URL}auth/register") {
            contentType(ContentType.Application.Json)
            setBody(authRequest)
        }.body()
    }

    suspend fun loginWithFirebase(request: FirebaseAuthRequest): AuthResponse {
        return client.post("${AppConfig.BASE_URL}auth/firebase") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

//    @POST("auth/firebase")
//    suspend fun loginWithFirebase(@Body firebaseAuthRequest: FirebaseAuthRequest): Response<AuthResponse>

    suspend fun login(authRequest: AuthRequest): AuthResponse {
        return client.post("${AppConfig.BASE_URL}auth/login") {
            contentType(ContentType.Application.Json)
            setBody(authRequest)
        }.body()
    }

    suspend fun refreshToken(tokenRequest: RefreshTokenRequest): AuthResponse {
        return client.post("${AppConfig.BASE_URL}auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(tokenRequest)
        }.body()
    }

    suspend fun verifyCode(email: String, code: String): AuthResponse {
        return client.post("${AppConfig.BASE_URL}auth/verify") {
            contentType(ContentType.Application.FormUrlEncoded) // тип формы
            setBody(Parameters.build {
                append("email", email)
                append("code", code)
            }.formUrlEncode())
        }.body()
    }

    //TODO: investigate this and add to the documentation
    suspend fun resendCode(email: String): AuthResponse {
        return client.post("${AppConfig.BASE_URL}auth/resend_code") {
            contentType(ContentType.Application.FormUrlEncoded) // тип формы
            setBody(Parameters.build {
                append("email", email)
            }.formUrlEncode())
        }.body()
    }

    suspend fun clearCode(email: String): Unit {
        return client.post("${AppConfig.BASE_URL}auth/clear_code") {
            contentType(ContentType.Application.FormUrlEncoded) // тип формы
            setBody(Parameters.build {
                append("email", email)
            }.formUrlEncode())
        }.body()
    }
    //end of region

    //user region

    suspend fun getUserById(uuid: String): UserEntity {
        return client.get {
            url("${AppConfig.BASE_URL}user/$uuid")
        }.body()
    }

    //end of region

    //course region

    suspend fun getCourseById(uuid: String): CourseEntity {
        return client.get {
            url("${AppConfig.BASE_URL}course/$uuid")
        }.body()
    }

    suspend fun getAllCourses(userId: String): List<CourseEntity> {
        return client.get("${AppConfig.BASE_URL}course/get") {
            parameter("userId", userId)
        }.body()
    }

    suspend fun addCourse(request: AddCourseRequest): CourseEntity {
        return client.post("${AppConfig.BASE_URL}course/add") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    //end of region

    //set region
    suspend fun getAllSets(courseId: String): List<SetResponse> {
        return client.get("${AppConfig.BASE_URL}sets/get") {
            parameter("courseId", courseId)
        }.body()
    }

    suspend fun saveSet(addSetRequest: AddSetRequest): SetResponse {
        return client.post("${AppConfig.BASE_URL}sets/add") {
            contentType(ContentType.Application.Json)
            setBody(addSetRequest)
        }.body()
    }
    //end of region

    //words region
    suspend fun saveWorld(addWordRequest: AddWordRequest): WordResponse {
        return client.post("${AppConfig.BASE_URL}words/add") {
            contentType(ContentType.Application.Json)
            setBody(addWordRequest)
        }.body()
    }

    suspend fun getWordByTranslated(courseId: String, translate: String): WordResponse {
        return client.get("${AppConfig.BASE_URL}words/check_by_translated") {
            parameter("courseId", courseId)
            parameter("translate", translate)
        }.body()
    }

    suspend fun getWordByOriginal(courseId: String, original: String): WordResponse {
        return client.get("${AppConfig.BASE_URL}words/check_by_origin") {
            parameter("courseId", courseId)
            parameter("original", original)
        }.body()
    }

    suspend fun getWordsBySet(setId: String): List<WordResponse> {
        return client.get {
            url("${AppConfig.BASE_URL}words/get_by_set/$setId")
        }.body()
    }

    suspend fun updateStatus(
        wordId: String,
        updateStatusRequest: UpdateWordStatusRequest
    ): WordResponse {
        return client.patch("${AppConfig.BASE_URL}words/update_status/$wordId") {
            contentType(ContentType.Application.Json)
            setBody(updateStatusRequest)
        }.body()
    }

    suspend fun deleteWord(wordId: String): Unit {
        client.delete("${AppConfig.BASE_URL}words/delete/$wordId")
    }

    //end of region

}