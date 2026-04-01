package data.api

import data.model.auth.AuthRequest
import data.model.auth.FirebaseAuthRequest
import data.model.auth.RefreshTokenRequest
import data.model.course.add.AddCourseRequest
import data.model.sets.AddSetRequest
import data.model.words.add.AddWordRequest
import data.model.words.update.UpdateWordStatusRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
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
    suspend fun register(authRequest: AuthRequest): HttpResponse {
        return client.post("${AppConfig.BASE_URL}auth/register") {
            contentType(ContentType.Application.Json)
            setBody(authRequest)
        }
    }

    suspend fun loginWithFirebase(request: FirebaseAuthRequest): HttpResponse {
        return client.post("${AppConfig.BASE_URL}auth/firebase") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

//    @POST("auth/firebase")
//    suspend fun loginWithFirebase(@Body firebaseAuthRequest: FirebaseAuthRequest): Response<AuthResponse>

    suspend fun login(authRequest: AuthRequest): HttpResponse {
        return client.post("${AppConfig.BASE_URL}auth/login") {
            contentType(ContentType.Application.Json)
            setBody(authRequest)
        }
    }

    suspend fun refreshToken(tokenRequest: RefreshTokenRequest): HttpResponse {
        return client.post("${AppConfig.BASE_URL}auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(tokenRequest)
        }
    }

    suspend fun verifyCode(email: String, code: String): HttpResponse {
        return client.post("${AppConfig.BASE_URL}auth/verify") {
            contentType(ContentType.Application.FormUrlEncoded) // тип формы
            setBody(Parameters.build {
                append("email", email)
                append("code", code)
            }.formUrlEncode())
        }
    }

    //TODO: investigate this and add to the documentation
    suspend fun resendCode(email: String): HttpResponse {
        return client.post("${AppConfig.BASE_URL}auth/resend_code") {
            contentType(ContentType.Application.FormUrlEncoded) // тип формы
            setBody(Parameters.build {
                append("email", email)
            }.formUrlEncode())
        }
    }

    suspend fun clearCode(email: String): HttpResponse {
        return client.post("${AppConfig.BASE_URL}auth/clear_code") {
            contentType(ContentType.Application.FormUrlEncoded) // тип формы
            setBody(Parameters.build {
                append("email", email)
            }.formUrlEncode())
        }
    }
    //end of region

    //user region

    suspend fun getUserById(uuid: String): HttpResponse {
        return client.get {
            url("${AppConfig.BASE_URL}user/$uuid")
        }
    }

    //end of region

    //course region

    suspend fun getCourseById(uuid: String): HttpResponse {
        return client.get {
            url("${AppConfig.BASE_URL}course/$uuid")
        }
    }

    suspend fun getAllCourses(userId: String): HttpResponse {
        return client.get("${AppConfig.BASE_URL}course/get") {
            parameter("userId", userId)
        }
    }

    suspend fun addCourse(request: AddCourseRequest): HttpResponse {
        return client.post("${AppConfig.BASE_URL}course/add") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    //end of region

    //set region
    suspend fun getAllSets(courseId: String): HttpResponse {
        return client.get("${AppConfig.BASE_URL}sets/get") {
            parameter("courseId", courseId)
        }
    }

    suspend fun saveSet(addSetRequest: AddSetRequest): HttpResponse {
        return client.post("${AppConfig.BASE_URL}sets/add") {
            contentType(ContentType.Application.Json)
            setBody(addSetRequest)
        }
    }
    //end of region

    //words region
    suspend fun saveWorld(addWordRequest: AddWordRequest): HttpResponse {
        return client.post("${AppConfig.BASE_URL}words/add") {
            contentType(ContentType.Application.Json)
            setBody(addWordRequest)
        }
    }

    suspend fun getWordByTranslated(courseId: String, translate: String): HttpResponse {
        return client.get("${AppConfig.BASE_URL}words/check_by_translated") {
            parameter("courseId", courseId)
            parameter("translate", translate)
        }
    }

    suspend fun getWordByOriginal(courseId: String, original: String): HttpResponse {
        return client.get("${AppConfig.BASE_URL}words/check_by_origin") {
            parameter("courseId", courseId)
            parameter("original", original)
        }
    }

    suspend fun getWordsBySet(setId: String): HttpResponse {
        return client.get {
            url("${AppConfig.BASE_URL}words/get_by_set/$setId")
        }
    }

    suspend fun updateStatus(
        wordId: String,
        updateStatusRequest: UpdateWordStatusRequest
    ): HttpResponse {
        return client.patch("${AppConfig.BASE_URL}words/update_status/$wordId") {
            contentType(ContentType.Application.Json)
            setBody(updateStatusRequest)
        }
    }

    suspend fun deleteWord(wordId: String): HttpResponse {
        return client.delete("${AppConfig.BASE_URL}words/delete/$wordId")
    }

    //end of region

}