package com.example.translatortrainer.di

import com.example.translatortrainer.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HistoryViewModel(get())
    }
}