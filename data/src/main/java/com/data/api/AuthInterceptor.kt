package com.data.api

import com.data.prefs.ITokenStorage
import com.data.prefs.TokenStorage.Companion.ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(
    private val tokenProvider: ITokenStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = tokenProvider.getToken(ACCESS_TOKEN)

        // Добавляем токен в заголовок, если он существует
        val requestWithToken = if (!accessToken.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(requestWithToken)
    }
}