package com.translator.app.di

import presentation.ui.screens.account.AccountViewModel
import presentation.ui.screens.all.AllWordsViewModel
import presentation.ui.screens.home.HomeViewModel
import presentation.ui.screens.newset.NewSetViewModel
import com.presentation.ui.screens.set.SetViewModel
import presentation.ui.screens.sets.SetsViewModel
import presentation.ui.screens.auth.AuthViewModel
import presentation.ui.screens.auth.verify.VerifyEmailViewModel
import presentation.ui.screens.lesson.bubble.BubbleLessonViewModel
import presentation.ui.screens.lesson.success.SuccessLessonViewModel
import presentation.ui.screens.select_course.SelectCourseViewModel
import presentation.ui.screens.splash.SplashViewModel
import com.presentation.ui.screens.texts.TextFromPhotoViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import presentation.viewmodel.MainViewModel
import org.koin.dsl.module
import presentation.ui.screens.lesson.match.MatchLessonViewModel


val viewModelModule = module {

    viewModel { SplashViewModel(get(), get(), get()) }
    viewModelOf(::MainViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::SetsViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::SetViewModel)
    viewModelOf(::AllWordsViewModel)
    viewModelOf(::NewSetViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::VerifyEmailViewModel)
    viewModelOf(::SelectCourseViewModel)
    viewModelOf(::BubbleLessonViewModel)
    viewModelOf(::MatchLessonViewModel)
    viewModelOf(::SuccessLessonViewModel)
    viewModelOf(::TextFromPhotoViewModel)
}