package data.api

import com.example.translatortrainer.shared.BuildConfig
import data.prefs.ITokenStorage
import domain.token.TokenRefresher.Companion.ACCESS_TOKEN
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual object AppConfig {
    actual val BASE_URL: String = BuildConfig.BASE_URL
}

actual fun provideHttpClient(tokenProvider: ITokenStorage): HttpClient {
    return HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true   // игнорировать лишние поля в JSON
                isLenient = true           // позволяет парсить слегка некорректный JSON
            })
        }

        install(Logging) {
            level = LogLevel.ALL // BODY, HEADERS, INFO, ALL
            logger = Logger.SIMPLE
        }

        defaultRequest {
            val token = tokenProvider.getToken(ACCESS_TOKEN)
            if (!token.isNullOrEmpty()) {
                header("Authorization", "Bearer $token")
            }
        }

        HttpResponseValidator {
            validateResponse { response ->
                if (response.status.value == 401) {
                    throw UnauthorizedException()
                }
            }
        }
    }
}