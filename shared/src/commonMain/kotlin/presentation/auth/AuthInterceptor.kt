package presentation.auth

import data.model.auth.FirebaseAuthRequest
import data.model.course.CourseEntity
import domain.usecases.auth.ILoginUseCase
import domain.usecases.auth.IRegisterUseCase
import domain.usecases.auth.IRegisterWithFirebaseUseCase
import domain.usecases.auth.ISetGuestUseCase
import domain.usecases.course.ICoursesOnPrefsUseCases
import domain.usecases.course.IGetAllCoursesUseCase

class AuthInteractor(
    private val login: ILoginUseCase,
    private val register: IRegisterUseCase,
    private val registerByFirebase: IRegisterWithFirebaseUseCase,
    private val getCourses: IGetAllCoursesUseCase,
    private val coursesPrefs: ICoursesOnPrefsUseCases,
    private val guestPrefs: ISetGuestUseCase
) {

    suspend fun login(email: String, password: String): AuthResult {
        val response = login.invoke(email, email, password)

        if (!response.isSuccess) {
            return mapError(response)
        }

        val userId = response.getOrNull()
            ?: return AuthResult.Error(BaseAuthError.USER_DOES_NOT_EXIST)

        return checkCourses(userId)
    }

    suspend fun register(email: String, password: String): AuthResult {
        val response = register.invoke(email, email, password)

        if (!response.isSuccess) {
            if (response.exceptionOrNull()?.message == "Verification is needed") {
                return AuthResult.NeedsVerification
            }
            return mapError(response)
        }

        return AuthResult.Success
    }

    suspend fun loginWithGoogle(data: FirebaseAuthRequest): AuthResult {
        val response = registerByFirebase.invoke(data)

        if (!response.isSuccess) {
            return mapError(response)
        }

        val userId = response.getOrNull()
            ?: return AuthResult.Error(BaseAuthError.USER_DOES_NOT_EXIST)

        return checkCourses(userId)
    }

    suspend fun continueAsGuest(): AuthResult {
        guestPrefs.setGuest()
        return AuthResult.Success
    }

    private suspend fun checkCourses(userId: String): AuthResult {
        val response = getCourses.invoke(userId)

        if (!response.isSuccess) {
            return mapError(response)
        }

        val courses = response.getOrNull() ?: return AuthResult.Error(BaseAuthError.DEFAULT)

        return if (courses.size != 1) {
            coursesPrefs.saveAll(courses)
            AuthResult.NeedsCourseSelection(courses)
        } else {
            coursesPrefs.saveOne(courses.first())
            AuthResult.GoToHome(courses.first())
        }
    }

    private fun <T> mapError(response: Result<T>): AuthResult.Error {
        val error = when (response.exceptionOrNull()?.message) {
            "Wrong password" -> BaseAuthError.WRONG_PASSWORD
            "User does not exist" -> BaseAuthError.USER_DOES_NOT_EXIST
            "User already exists" -> BaseAuthError.EMAIL_TAKEN
            "Failed to connect" -> BaseAuthError.INTERNET_CONNECTION_ERROR
            else -> BaseAuthError.DEFAULT
        }
        return AuthResult.Error(error)
    }
}

sealed class AuthResult {
    object Success : AuthResult()
    object NeedsVerification : AuthResult()

    data class NeedsCourseSelection(val courses: List<CourseEntity>) : AuthResult()
    data class GoToHome(val course: CourseEntity) : AuthResult()

    data class Error(val error: BaseAuthError) : AuthResult()
}

enum class BaseAuthError {
    EMPTY_FIELDS,
    USER_DOES_NOT_EXIST,
    WRONG_PASSWORD,
    EMAIL_TAKEN,
    INTERNET_CONNECTION_ERROR,
    DEFAULT
}
