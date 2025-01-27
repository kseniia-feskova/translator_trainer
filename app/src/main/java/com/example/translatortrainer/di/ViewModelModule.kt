package com.example.translatortrainer.di

import com.example.translatortrainer.test.ITestDataHelper
import com.example.translatortrainer.test.TestDataHelper
import com.example.translatortrainer.viewmodel.CardSetViewModel
import com.example.translatortrainer.viewmodel.TranslatorViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val appModule = module {
    singleOf(::TestDataHelper) bind ITestDataHelper::class
}

val viewModelModule = module {
    viewModelOf(::TranslatorViewModel)
    viewModelOf(::CardSetViewModel)
}