package com.data.api

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Response
import okhttp3.Request
import okhttp3.Route

class TokenAuthenticator(
    private val refreshTokenProvider: suspend () -> String,
    private val onAccessTokenRefreshed: (String) -> Unit
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        // Получаем новый токен
        val newAccessToken = runBlocking { refreshTokenProvider() }

        // Обновляем токен в хранилище
        onAccessTokenRefreshed(newAccessToken)

        // Повторяем запрос с новым токеном
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }
}