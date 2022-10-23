package com.example.translatortrainer.di

import android.app.Application
import androidx.room.Room
import com.example.translatortrainer.data.MainRepository
import com.example.translatortrainer.room.HistoryDAO
import com.example.translatortrainer.room.HistoryDatabase
import com.example.translatortrainer.utils.CustomTranslator
import com.example.translatortrainer.viewmodel.HistoryViewModel
import com.example.translatortrainer.viewmodel.TranslationViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel {
       // TranslationViewModel(get())
        HistoryViewModel(get())
    }
}

val databaseModule = module {

    fun provideDatabase(application: Application): HistoryDatabase {
        return HistoryDatabase.getInstance(application)
    }


    fun provideDao(database: HistoryDatabase): HistoryDAO {
        return database.historyDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
}

val translatorModule = module{
    single { CustomTranslator() }
}

val repositoryModule = module {
    fun provideMainRepository(dao: HistoryDAO, translator: CustomTranslator): MainRepository {
        return MainRepository(dao, translator)
    }

    single { provideMainRepository(get(), get()) }
}


//val apiModule = module {
//    fun provideUseApi(retrofit: Retrofit): GithubApi {
//        return retrofit.create(GithubApi::class.java)
//    }
//
//    single { provideUseApi(get()) }
//}
//
//val retrofitModule = module {
//
//    fun provideGson(): Gson {
//        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
//    }
//
//    fun provideHttpClient(): OkHttpClient {
//        val okHttpClientBuilder = OkHttpClient.Builder()
//
//        return okHttpClientBuilder.build()
//    }
//
//    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl("https://api.github.com/")
//            .addConverterFactory(GsonConverterFactory.create(factory))
//            .client(client)
//            .build()
//    }
//
//    single { provideGson() }
//    single { provideHttpClient() }
//    single { provideRetrofit(get(), get()) }

