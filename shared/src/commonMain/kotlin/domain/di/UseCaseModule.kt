package domain.di

import domain.cache.ISetsCacheProvider
import domain.cache.SetsCacheProvider
import domain.token.ITokenRefresher
import domain.token.TokenRefresher
import domain.usecases.GetAccountUseCase
import domain.usecases.IAccountUseCase
import domain.usecases.IGetAccountUseCase
import domain.usecases.ITranslateWordUseCase
import domain.usecases.TranslateWordUseCase
import domain.usecases.auth.CheckUserUseCase
import domain.usecases.auth.CreateFromGuestUseCase
import domain.usecases.auth.DeleteUseCase
import domain.usecases.auth.ICheckUserUseCase
import domain.usecases.auth.ICreateFromGuestUseCase
import domain.usecases.auth.IDeleteUseCase
import domain.usecases.auth.ILoginUseCase
import domain.usecases.auth.ILogoutUseCase
import domain.usecases.auth.IRegisterUseCase
import domain.usecases.auth.IRegisterWithFirebaseUseCase
import domain.usecases.auth.ISetGuestUseCase
import domain.usecases.auth.LoginUseCase
import domain.usecases.auth.LogoutUseCase
import domain.usecases.auth.RegisterUseCase
import domain.usecases.auth.RegisterWithFirebaseUseCase
import domain.usecases.auth.SetGuestUseCase
import domain.usecases.auth.verify.DeleteCodeUseCase
import domain.usecases.auth.verify.IDeleteCodeUseCase
import domain.usecases.auth.verify.IResendCodeUseCase
import domain.usecases.auth.verify.IVerifyCodeUseCase
import domain.usecases.auth.verify.ResendCodeUseCase
import domain.usecases.auth.verify.VerifyCodeUseCase
import domain.usecases.course.AddCourseUseCase
import domain.usecases.course.CoursesOnPrefsUseCases
import domain.usecases.course.GetAllCoursesUseCase
import domain.usecases.course.GetCourseUseCase
import domain.usecases.course.IAddCourseUseCase
import domain.usecases.course.ICoursesOnPrefsUseCases
import domain.usecases.course.IGetAllCoursesUseCase
import domain.usecases.course.IGetCourseUseCase
import domain.usecases.sets.AddSetUseCase
import domain.usecases.sets.GetAllSetsUseCase
import domain.usecases.sets.GetAllWordsIdUseCase
import domain.usecases.sets.IAddSetUseCase
import domain.usecases.sets.IGetAllSetsUseCase
import domain.usecases.sets.IGetAllWordsIdUseCase
import domain.usecases.sets.IUpdateSetsUseCase
import domain.usecases.sets.UpdateSetsUseCase
import domain.usecases.user.AccountUseCase
import domain.usecases.words.AddWordByApiUseCase
import domain.usecases.words.AddWordUseCase
import domain.usecases.words.DeleteWordUseCase
import domain.usecases.words.GetWordByOriginal
import domain.usecases.words.GetWordByTranslated
import domain.usecases.words.GetWordsBySetUseCase
import domain.usecases.words.IAddWordByApiUseCase
import domain.usecases.words.IAddWordUseCase
import domain.usecases.words.IDeleteWordUseCase
import domain.usecases.words.IGetWordByOriginal
import domain.usecases.words.IGetWordByTranslated
import domain.usecases.words.IGetWordsBySetUseCase
import domain.usecases.words.IUpdateStatusUseCase
import domain.usecases.words.UpdateStatusUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val useCaseModule = module {

    singleOf(::LoginUseCase) bind ILoginUseCase::class

    singleOf(::SetsCacheProvider) bind ISetsCacheProvider::class

    singleOf(::RegisterUseCase) bind IRegisterUseCase::class

    singleOf(::RegisterWithFirebaseUseCase) bind IRegisterWithFirebaseUseCase::class

    singleOf(::VerifyCodeUseCase) bind IVerifyCodeUseCase::class

    singleOf(::ResendCodeUseCase) bind IResendCodeUseCase::class

    singleOf(::DeleteCodeUseCase) bind IDeleteCodeUseCase::class

    singleOf(::AddCourseUseCase) bind IAddCourseUseCase::class

    singleOf(::AddWordUseCase) bind IAddWordUseCase::class

    singleOf(::AddWordByApiUseCase) bind IAddWordByApiUseCase::class

    singleOf(::TranslateWordUseCase) bind ITranslateWordUseCase::class

    singleOf(::GetAllSetsUseCase) bind IGetAllSetsUseCase::class

    singleOf(::GetAllWordsIdUseCase) bind IGetAllWordsIdUseCase::class

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

    singleOf(::CheckUserUseCase) bind ICheckUserUseCase::class
}