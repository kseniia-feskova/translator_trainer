package com.data.di

import com.data.api.TranslateService
import com.data.repository.translate.TranslateRepository
import com.data.translate.CustomTranslator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val translatorModule = module {
    single { CustomTranslator() }
}

// Создаем логгер для логирования тела запросов и ответов
val logging = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val bankClient = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()


val networkModule = module {
    single {
        Retrofit.Builder()
            .client(bankClient)
            .baseUrl("https://api.mymemory.translated.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslateService::class.java)
    }
    single { TranslateRepository(get()) }
}
