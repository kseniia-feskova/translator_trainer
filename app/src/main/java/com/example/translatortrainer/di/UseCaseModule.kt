package com.example.translatortrainer.di

import com.domain.cache.SetsCacheProvider
import com.domain.token.ITokenRefresher
import com.domain.token.TokenRefresher
import com.domain.usecase.GetAccountUseCase
import com.domain.usecase.TranslateWordUseCase
import com.domain.usecase.auth.CreateFromGuestUseCase
import com.domain.usecase.auth.DeleteUseCase
import com.domain.usecase.auth.LoginUseCase
import com.domain.usecase.auth.LogoutUseCase
import com.domain.usecase.auth.RegisterUseCase
import com.domain.usecase.auth.SetGuestUseCase
import com.domain.usecase.auth.verify.DeleteCodeUseCase
import com.domain.usecase.auth.verify.ResendCodeUseCase
import com.domain.usecase.auth.verify.VerifyCodeUseCase
import com.domain.usecase.course.AddCourseUseCase
import com.domain.usecase.course.CoursesOnPrefsUseCases
import com.domain.usecase.course.GetAllCoursesUseCase
import com.domain.usecase.course.GetCourseUseCase
import com.domain.usecase.sets.AddSetUseCase
import com.domain.usecase.sets.GetAllSetsUseCase
import com.domain.usecase.sets.UpdateSetsUseCase
import com.domain.usecase.user.AccountUseCase
import com.domain.usecase.words.AddWordByApiUseCase
import com.domain.usecase.words.AddWordUseCase
import com.domain.usecase.words.DeleteWordUseCase
import com.domain.usecase.words.GetWordByOriginal
import com.domain.usecase.words.GetWordByTranslated
import com.domain.usecase.words.GetWordsBySetUseCase
import com.domain.usecase.words.UpdateStatusUseCase
import com.presentation.cache.ISetsCacheProvider
import com.presentation.usecases.IAccountUseCase
import com.presentation.usecases.IGetAccountUseCase
import com.presentation.usecases.ITranslateWordUseCase
import com.presentation.usecases.auth.ICreateFromGuestUseCase
import com.presentation.usecases.auth.IDeleteUseCase
import com.presentation.usecases.auth.ILoginUseCase
import com.presentation.usecases.auth.ILogoutUseCase
import com.presentation.usecases.auth.IRegisterUseCase
import com.presentation.usecases.auth.ISetGuestUseCase
import com.presentation.usecases.auth.verify.IDeleteCodeUseCase
import com.presentation.usecases.auth.verify.IResendCodeUseCase
import com.presentation.usecases.auth.verify.IVerifyCodeUseCase
import com.presentation.usecases.course.IAddCourseUseCase
import com.presentation.usecases.course.ICoursesOnPrefsUseCases
import com.presentation.usecases.course.IGetAllCoursesUseCase
import com.presentation.usecases.course.IGetCourseUseCase
import com.presentation.usecases.sets.IAddSetUseCase
import com.presentation.usecases.sets.IGetAllSetsUseCase
import com.presentation.usecases.sets.IUpdateSetsUseCase
import com.presentation.usecases.words.IAddWordByApiUseCase
import com.presentation.usecases.words.IAddWordUseCase
import com.presentation.usecases.words.IDeleteWordUseCase
import com.presentation.usecases.words.IGetWordByOriginal
import com.presentation.usecases.words.IGetWordByTranslated
import com.presentation.usecases.words.IGetWordsBySetUseCase
import com.presentation.usecases.words.IUpdateStatusUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val useCaseModule = module {

    singleOf(::SetsCacheProvider) bind ISetsCacheProvider::class

    singleOf(::RegisterUseCase) bind IRegisterUseCase::class

    singleOf(::LoginUseCase) bind ILoginUseCase::class

    singleOf(::VerifyCodeUseCase) bind IVerifyCodeUseCase::class

    singleOf(::ResendCodeUseCase) bind IResendCodeUseCase::class

    singleOf(::DeleteCodeUseCase) bind IDeleteCodeUseCase::class

    singleOf(::AddCourseUseCase) bind IAddCourseUseCase::class

    singleOf(::AddWordUseCase) bind IAddWordUseCase::class

    singleOf(::AddWordByApiUseCase) bind IAddWordByApiUseCase::class

    singleOf(::TranslateWordUseCase) bind ITranslateWordUseCase::class

    singleOf(::GetAllSetsUseCase) bind IGetAllSetsUseCase::class

    singleOf(::DeleteWordUseCase) bind IDeleteWordUseCase::class

    singleOf(::LogoutUseCase) bind ILogoutUseCase::class

    singleOf(::DeleteUseCase) bind IDeleteUseCase::class

    singleOf(::GetAccountUseCase) bind IGetAccountUseCase::class

    singleOf(::AccountUseCase) bind IAccountUseCase::class

    singleOf(::TokenRefresher) bind ITokenRefresher::class

    singleOf(::GetCourseUseCase) bind IGetCourseUseCase::class

    singleOf(::GetWordByTranslated) bind IGetWordByTranslated::class

    singleOf(::GetWordByOriginal) bind IGetWordByOriginal::class

    singleOf(::GetAllCoursesUseCase) bind IGetAllCoursesUseCase::class

    singleOf(::CoursesOnPrefsUseCases) bind ICoursesOnPrefsUseCases::class

    singleOf(::GetWordsBySetUseCase) bind IGetWordsBySetUseCase::class

    singleOf(::AddSetUseCase) bind IAddSetUseCase::class

    singleOf(::UpdateStatusUseCase) bind IUpdateStatusUseCase::class

    singleOf(::UpdateSetsUseCase) bind IUpdateSetsUseCase::class

    singleOf(::SetGuestUseCase) bind ISetGuestUseCase::class

    singleOf(::CreateFromGuestUseCase) bind ICreateFromGuestUseCase::class
}