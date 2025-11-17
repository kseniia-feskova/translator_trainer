import com.presentation.usecases.ITranslateWordUseCase
import presentation.usecases.auth.ICreateFromGuestUseCase
import com.presentation.usecases.auth.IDeleteUseCase
import com.presentation.usecases.auth.ILogoutUseCase
import com.presentation.usecases.auth.verify.IDeleteCodeUseCase
import com.presentation.usecases.auth.verify.IResendCodeUseCase
import com.presentation.usecases.course.IAddCourseUseCase
import com.presentation.usecases.sets.IUpdateSetsUseCase
import com.presentation.usecases.words.IAddWordByApiUseCase
import com.presentation.usecases.words.IAddWordUseCase
import presentation.usecases.auth.verify.IVerifyCodeUseCase
import presentation.usecases.course.IGetCourseUseCase
import com.presentation.usecases.words.IGetWordByOriginal
import com.presentation.usecases.words.IGetWordByTranslated
import domain.cache.ISetsCacheProvider
import domain.cache.SetsCacheProvider
import domain.token.ITokenRefresher
import domain.token.TokenRefresher
import usecase.GetAccountUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import presentation.usecases.IAccountUseCase
import presentation.usecases.IGetAccountUseCase
import presentation.usecases.auth.ILoginUseCase
import presentation.usecases.auth.IRegisterUseCase
import presentation.usecases.auth.IRegisterWithFirebaseUseCase
import presentation.usecases.auth.ISetGuestUseCase
import presentation.usecases.course.ICoursesOnPrefsUseCases
import presentation.usecases.course.IGetAllCoursesUseCase
import presentation.usecases.sets.IAddSetUseCase
import presentation.usecases.sets.IGetAllSetsUseCase
import presentation.usecases.words.IDeleteWordUseCase
import presentation.usecases.words.IGetWordsBySetUseCase
import presentation.usecases.words.IUpdateStatusUseCase
import usecase.auth.LoginUseCase
import usecase.auth.RegisterUseCase
import usecase.auth.LogoutUseCase
import usecase.auth.DeleteUseCase
import usecase.auth.verify.VerifyCodeUseCase
import usecase.auth.verify.ResendCodeUseCase
import usecase.auth.verify.DeleteCodeUseCase
import usecase.course.GetCourseUseCase
import usecase.course.AddCourseUseCase
import usecase.course.CoursesOnPrefsUseCases
import usecase.course.GetAllCoursesUseCase
import usecase.words.AddWordUseCase
import usecase.words.GetWordByOriginal
import usecase.words.GetWordByTranslated
import usecase.words.DeleteWordUseCase
import usecase.words.AddWordByApiUseCase
import usecase.words.GetWordsBySetUseCase
import usecase.words.UpdateStatusUseCase
import usecase.TranslateWordUseCase
import usecase.sets.GetAllSetsUseCase
import usecase.sets.AddSetUseCase
import usecase.sets.UpdateSetsUseCase
import usecase.user.AccountUseCase
import usecase.auth.SetGuestUseCase
import usecase.auth.CreateFromGuestUseCase
import usecase.auth.RegisterWithFirebaseUseCase

val useCaseModule = module {

    singleOf(::SetsCacheProvider) bind ISetsCacheProvider::class

    singleOf(::RegisterUseCase) bind IRegisterUseCase::class

    singleOf(::RegisterWithFirebaseUseCase) bind IRegisterWithFirebaseUseCase::class

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