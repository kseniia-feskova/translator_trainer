package com.translator.app.di

import com.translator.app.ui.screens.account.AccountViewModel
import com.translator.app.ui.screens.all.AllWordsViewModel
import com.translator.app.ui.screens.home.HomeViewModel
import com.translator.app.ui.screens.newset.NewSetViewModel
import com.presentation.ui.screens.set.SetViewModel
import com.translator.app.ui.screens.sets.SetsViewModel
import com.translator.app.ui.screens.auth.AuthViewModel
import com.translator.app.ui.screens.auth.verify.VerifyEmailViewModel
import com.translator.app.ui.screens.lesson.bubble.BubbleLessonViewModel
import com.translator.app.ui.screens.lesson.success.SuccessLessonViewModel
import com.translator.app.ui.screens.select_course.SelectCourseViewModel
import com.translator.app.ui.screens.splash.SplashViewModel
import com.translator.app.ui.screens.texts.TextFromPhotoViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import presentation.viewmodel.MainViewModel
import org.koin.dsl.module
import com.translator.app.ui.screens.lesson.dictation.DictationLessonViewModel
import com.translator.app.ui.screens.lesson.dictation.result.DictationResultViewModel
import com.translator.app.ui.screens.lesson.match.MatchLessonViewModel


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
    viewModelOf(::DictationLessonViewModel)
    viewModelOf(::DictationResultViewModel)
    viewModelOf(::SuccessLessonViewModel)
    viewModelOf(::TextFromPhotoViewModel)
}