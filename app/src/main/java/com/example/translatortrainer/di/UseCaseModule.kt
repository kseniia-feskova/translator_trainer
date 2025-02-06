package com.example.translatortrainer.di

import com.domain.token.ITokenRefresher
import com.domain.token.TokenRefresher
import com.domain.usecase.AddSetWordCrossRef
import com.domain.usecase.AddWordToSetUseCase
import com.domain.usecase.DeleteSetByIdUseCase
import com.domain.usecase.GetAccountUseCase
import com.domain.usecase.GetAllSetsUseCase
import com.domain.usecase.TranslateWordUseCase
import com.domain.usecase.auth.DeleteUseCase
import com.domain.usecase.auth.LoginUseCase
import com.domain.usecase.auth.LogoutUseCase
import com.domain.usecase.auth.RegisterUseCase
import com.domain.usecase.course.AddCourseUseCase
import com.domain.usecase.course.GetAllCoursesUseCase
import com.domain.usecase.course.GetCourseUseCase
import com.domain.usecase.words.AddWordUseCase
import com.domain.usecase.words.DeleteWordUseCase
import com.domain.usecase.words.GetWordByIdUseCase
import com.domain.usecase.words.GetWordByOriginal
import com.domain.usecase.words.GetWordByTranslated
import com.domain.usecase.words.GetWordsOfSetUseCase
import com.domain.usecase.words.UpdateWordUseCase
import com.presentation.usecases.IAddSetWordCrossRefUseCase
import com.presentation.usecases.IAddWordToSetUseCase
import com.presentation.usecases.IDeleteSetByIdUseCase
import com.presentation.usecases.IGetAccountUseCase
import com.presentation.usecases.IGetAllSetsUseCase
import com.presentation.usecases.ITranslateWordUseCase
import com.presentation.usecases.auth.IDeleteUseCase
import com.presentation.usecases.auth.ILoginUseCase
import com.presentation.usecases.auth.ILogoutUseCase
import com.presentation.usecases.auth.IRegisterUseCase
import com.presentation.usecases.course.IAddCourseUseCase
import com.presentation.usecases.course.IGetAllCoursesUseCase
import com.presentation.usecases.course.IGetCourseUseCase
import com.presentation.usecases.words.IAddWordUseCase
import com.presentation.usecases.words.IDeleteWordUseCase
import com.presentation.usecases.words.IGetWordByIdUseCase
import com.presentation.usecases.words.IGetWordByOriginal
import com.presentation.usecases.words.IGetWordByTranslated
import com.presentation.usecases.words.IGetWordsOfSetUseCase
import com.presentation.usecases.words.IUpdateWordUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val useCaseModule = module {

    singleOf(::RegisterUseCase) bind IRegisterUseCase::class

    singleOf(::LoginUseCase) bind ILoginUseCase::class

    singleOf(::AddCourseUseCase) bind IAddCourseUseCase::class

    singleOf(::AddWordUseCase) bind IAddWordUseCase::class

    singleOf(::GetWordsOfSetUseCase) bind IGetWordsOfSetUseCase::class

    singleOf(::AddSetWordCrossRef) bind IAddSetWordCrossRefUseCase::class

    singleOf(::UpdateWordUseCase) bind IUpdateWordUseCase::class

    singleOf(::GetWordByIdUseCase) bind IGetWordByIdUseCase::class

    singleOf(::TranslateWordUseCase) bind ITranslateWordUseCase::class

    singleOf(::DeleteSetByIdUseCase) bind IDeleteSetByIdUseCase::class

    singleOf(::GetAllSetsUseCase) bind IGetAllSetsUseCase::class

    singleOf(::DeleteWordUseCase) bind IDeleteWordUseCase::class

    singleOf(::AddWordToSetUseCase) bind IAddWordToSetUseCase::class

    singleOf(::LogoutUseCase) bind ILogoutUseCase::class

    singleOf(::DeleteUseCase) bind IDeleteUseCase::class

    singleOf(::GetAccountUseCase) bind IGetAccountUseCase::class

    singleOf(::TokenRefresher) bind ITokenRefresher::class

    singleOf(::GetCourseUseCase) bind IGetCourseUseCase::class

    singleOf(::GetWordByTranslated) bind IGetWordByTranslated::class

    singleOf(::GetWordByOriginal) bind IGetWordByOriginal::class

    singleOf(::GetAllCoursesUseCase) bind IGetAllCoursesUseCase::class

}