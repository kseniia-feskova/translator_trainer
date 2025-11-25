package com.translator.app.di

import data.api.TranslateService
import domain.translate.ITranslateModelProvider
import domain.translate.TranslateModelProvider
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val MY_MEMORY_API = "https://api.mymemory.translated.net"
val translateModule = module {
    //TODO: Remove?
    single<TranslateService> {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl(MY_MEMORY_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslateService::class.java)
    }

    singleOf(::TranslateModelProvider) bind ITranslateModelProvider::class

}
