package com.example.translatortrainer.di

import com.presentation.viewmodel.CardSetViewModel
import com.presentation.viewmodel.MainViewModel
import com.presentation.viewmodel.TranslatorViewModel
import com.presentation.viewmodel.courses.SelectSecondTranslateViewModel
import com.presentation.viewmodel.courses.SelectTranslateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    // singleOf(::TestDataHelper) bind ITestDataHelper::class
}

val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get(), get(), get()) }
    viewModelOf(::TranslatorViewModel)
    viewModel { (setId: Int) ->
        CardSetViewModel(
            setId,
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel { (setId: Int) ->
        SelectTranslateViewModel(
            setId,
            get(),
            get(),
            get()
        )
    }
    viewModel { (setId: Int) ->
        SelectSecondTranslateViewModel(
            setId,
            get(),
            get(),
            get()
        )
    }
}