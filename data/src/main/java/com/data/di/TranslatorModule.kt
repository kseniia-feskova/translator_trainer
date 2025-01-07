package com.data.di

import com.data.api.TranslateService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val translateModule = module {
    single<TranslateService> {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl("https://api.mymemory.translated.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslateService::class.java)
    }
}
