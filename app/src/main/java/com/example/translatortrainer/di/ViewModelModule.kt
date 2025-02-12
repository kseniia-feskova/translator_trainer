package com.example.translatortrainer.di

import com.presentation.ui.screens.account.AccountViewModel
import com.presentation.ui.screens.all.AllWordsViewModel
import com.presentation.ui.screens.home.HomeViewModel
import com.presentation.ui.screens.lesson.LessonViewModel
import com.presentation.ui.screens.newset.NewSetViewModel
import com.presentation.ui.screens.set.SetViewModel
import com.presentation.ui.screens.sets.SetsViewModel
import com.presentation.ui.screens.auth.AuthViewModel
import com.presentation.ui.screens.lesson.bubble.BubbleLessonViewModel
import com.presentation.ui.screens.select_course.SelectCourseViewModel
import com.presentation.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::SetsViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::SetViewModel)
    viewModelOf(::LessonViewModel)
    viewModelOf(::AllWordsViewModel)
    viewModelOf(::NewSetViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::SelectCourseViewModel)
    viewModelOf(::BubbleLessonViewModel)
}