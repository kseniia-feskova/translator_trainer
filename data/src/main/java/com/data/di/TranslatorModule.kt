package com.data.di

import com.data.api.TranslateService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single<TranslateService> {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl("https://api.mymemory.translated.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslateService::class.java)
    }
}
