package com.example.translatortrainer.di

import com.presentation.ui.screens.home.HomeViewModel
import com.presentation.ui.screens.lesson.LessonViewModel
import com.presentation.ui.screens.set.SetViewModel
import com.presentation.ui.screens.sets.SetsViewModel
import com.presentation.viewmodel.MainViewModel
import com.presentation.viewmodel.courses.SelectSecondTranslateViewModel
import com.presentation.viewmodel.courses.SelectTranslateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get()) }
    viewModelOf(::HomeViewModel)
    viewModelOf(::SetsViewModel)
    viewModelOf(::SetViewModel)
    viewModelOf(::LessonViewModel)

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