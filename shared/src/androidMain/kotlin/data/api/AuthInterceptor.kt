package data.api

import data.prefs.ITokenStorage
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(
    private val tokenProvider: ITokenStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = tokenProvider.getToken(ITokenStorage.ACCESS_TOKEN)

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