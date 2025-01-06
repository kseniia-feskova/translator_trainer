package com.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.data.api.ApiService
import com.data.api.AuthInterceptor
import com.data.prefs.ITokenStorage
import com.data.prefs.TokenStorage
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:8080/api/"

fun provideOkHttpClient(tokenProvider: ITokenStorage): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenProvider))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)// Замените на ваш URL
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}

val networkModule = module {

    single<SharedPreferences> {
        val masterKey = MasterKey.Builder(get<Context>())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            get<Context>(),
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // Предоставление LocalDataSource
    single<ITokenStorage> { TokenStorage(get()) }
    single { provideOkHttpClient(get()) } // Создает OkHttpClient
    single { provideRetrofit(get()) } // Создает Retrofit
    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }
}