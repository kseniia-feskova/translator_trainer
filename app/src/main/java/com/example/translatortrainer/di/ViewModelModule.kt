package com.example.translatortrainer.di

import com.presentation.ui.screens.all.AllWordsViewModel
import com.presentation.ui.screens.home.HomeViewModel
import com.presentation.ui.screens.lesson.LessonViewModel
import com.presentation.ui.screens.newset.NewSetViewModel
import com.presentation.ui.screens.set.SetViewModel
import com.presentation.ui.screens.sets.SetsViewModel
import com.presentation.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::SetsViewModel)
    viewModelOf(::SetViewModel)
    viewModelOf(::LessonViewModel)
    viewModelOf(::AllWordsViewModel)
    viewModelOf(::NewSetViewModel)
}