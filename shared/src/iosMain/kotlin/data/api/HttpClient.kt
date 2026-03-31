package data.api

import data.prefs.ITokenStorage
import domain.token.TokenRefresher.Companion.ACCESS_TOKEN
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json

actual object AppConfig {
    actual val BASE_URL: String = "https://your-production-api.com/"
}

actual fun provideHttpClient(tokenProvider: ITokenStorage): HttpClient {
    return HttpClient(Darwin) {
        install(ContentNegotiation) {
            json()
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