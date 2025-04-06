package translator.data.di

import translator.data.api.TranslateService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val GOOGLE_API = "https://translate.googleapis.com"
private const val MY_MEMORY_API = "https://api.mymemory.translated.net"
val translateModule = module {
    single<TranslateService> {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl(MY_MEMORY_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslateService::class.java)
    }
}
