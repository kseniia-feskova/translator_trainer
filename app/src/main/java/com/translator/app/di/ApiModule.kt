package com.translator.app.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.translator.app.BuildConfig
import com.translator.app.network.NetworkConnectivityObserver
import data.api.ApiService
import data.api.AuthInterceptor
import data.prefs.TokenStorage
import network.INetworkConnectivityObserver
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = BuildConfig.BASE_URL

fun provideOkHttpClient(tokenProvider: data.prefs.ITokenStorage): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Логировать тело запросов и ответов
    }
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
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
    single<data.prefs.ITokenStorage> { TokenStorage(get()) }
    single { provideOkHttpClient(get()) } // Создает OkHttpClient
    single { provideRetrofit(get()) } // Создает Retrofit
    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }

    single<INetworkConnectivityObserver> { NetworkConnectivityObserver(get<Context>()) }
}