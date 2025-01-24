package com.presentation.ui.screens.start

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.R
import com.presentation.data.IDataStoreManager
import com.presentation.model.CoursePreview
import com.presentation.model.CourseUI
import com.presentation.usecases.IGetAccountUseCase
import com.presentation.usecases.auth.ILoginUseCase
import com.presentation.usecases.auth.IRegisterUseCase
import com.presentation.usecases.course.IGetAllCoursesUseCase
import com.presentation.usecases.course.IGetCourseUseCase
import com.presentation.utils.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class LoginViewModel(
    private val register: IRegisterUseCase,
    private val login: ILoginUseCase,
    private val getUser: IGetAccountUseCase,
    private val getCourse: IGetCourseUseCase,
    private val dataStorage: IDataStoreManager,
    private val getAllCourses: IGetAllCoursesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()

    private var listOfCourses = emptyList<CoursePreview>()

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> onEmailChanged(intent.newEmail)
            is LoginIntent.EnterClicked -> onEnterClicked(intent.onLoginSuccess)
            is LoginIntent.PasswordChanged -> onPasswordChanged(intent.newPassword)
            is LoginIntent.RepeatPasswordChanged -> onRepeatPasswordChanged(intent.newRepeatPassword)
            LoginIntent.SwitchAuth -> onSwitchAuth()
            is LoginIntent.UsernameChanged -> onUserNameChanged(intent.newUsername)
            is LoginIntent.SelectCourse -> onCourseSelect(intent.course)
            LoginIntent.HideCourses -> {
                _uiState.update { it.copy(listOfCourses = null) }
            }
        }
    }

    private fun onEnterClicked(
        onLoginSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (_uiState.value.isLogin) {
                val userId = dataStorage.listenUserId().last()
                val course = _uiState.value.selectedCourse
                if (userId != null && course != null) {
                    Log.e("LoginViewModel", "saveCourse and login")
                    saveCourse(userId, course)
                    onLoginSuccess()
                } else {
                    Log.e("LoginViewModel", "handle Login on enter")
                    handleLogin(_uiState.value) { onLoginSuccess() }
                }
            } else {
                handleRegister(_uiState.value) { onLoginSuccess() }
            }
        }
    }

    private suspend fun handleLogin(state: LoginUIState, onLoginSuccess: () -> Unit) {
        val authResponse = login.invoke(state.email, state.username, state.password)
        if (authResponse.isSuccess) {
            val userId = authResponse.getOrNull()
            dataStorage.saveUserId(userId)
            if (userId != null) {
                checkSelectedCourse(userId) { onLoginSuccess() }
            }
        } else {
            Log.e("handleLogin", "Error = ${authResponse.exceptionOrNull()?.message}")
            _uiState.update { it.copy(error = "${authResponse.exceptionOrNull()?.message}") }
        }
    }

    private suspend fun handleRegister(state: LoginUIState, onLoginSuccess: () -> Unit) {
        val authResponse = register(state.selectedCourse, state)
        if (authResponse?.isSuccess == true) {
            val userId = authResponse.getOrNull()
            dataStorage.saveUserId(userId)
            if (state.selectedCourse != null) {
                saveCourse(userId, state.selectedCourse)
            }
            onLoginSuccess()
        } else {
            Log.e("handleLogin", "Error = ${authResponse?.exceptionOrNull()?.message}")
            _uiState.update { it.copy(error = "${authResponse?.exceptionOrNull()?.message}") }
        }
    }

    private suspend fun register(
        selectedCourse: CoursePreview?,
        state: LoginUIState
    ): Result<UUID?>? {
        return if (selectedCourse != null) {
            register.invoke(
                state.email,
                state.username,
                state.password,
                selectedCourse.originalLanguage,
                selectedCourse.translateLanguage
            )
        } else null
    }

    private suspend fun saveCourse(userId: UUID?, course: CoursePreview) {
        if (userId == null) return
        val lastCourseId = getUser.invoke(userId).getOrNull()?.courses?.last()
        val lastCourse = getCourse.invoke(userId).getOrNull()
        dataStorage.saveCourseId(lastCourseId)
        dataStorage.setAllWordsSetId(lastCourse?.allWordsId)
        dataStorage.setOriginalLanguage(course.originalLanguage)
        dataStorage.setResultLanguage(course.translateLanguage)
    }

    private fun onEmailChanged(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, error = null) }
        validate()
    }

    private fun onUserNameChanged(newUsername: String) {
        _uiState.update { it.copy(username = newUsername, error = null) }
        validate()
    }

    private fun onPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, error = null) }
        validate()
    }

    private fun onRepeatPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(repeatPassword = newPassword, error = null) }
        validate()
    }

    private fun onSwitchAuth() {
        _uiState.update {
            it.copy(
                isLogin = !it.isLogin,
                email = "",
                password = "",
                repeatPassword = "",
                error = null
            )
        }
        validate()
    }

    private fun onCourseSelect(course: CoursePreview) {
        _uiState.update { it.copy(selectedCourse = course) }
        validate()
    }

    private fun validate() {
        _uiState.update { it.copy(isValid = if (it.isLogin) checkForLogin(it) else checkForSignUp(it)) }
    }

    private fun checkForLogin(state: LoginUIState): Boolean {
        if (state.listOfCourses != null) return checkForLoginWihCourse(state)
        return state.email.isNotEmpty() && state.password.isNotEmpty() && state.username.isNotEmpty()
    }

    private fun checkForLoginWihCourse(state: LoginUIState): Boolean {
        return state.email.isNotEmpty() && state.password.isNotEmpty() && state.username.isNotEmpty() && state.selectedCourse != null
    }

    private fun checkForSignUp(state: LoginUIState): Boolean {
        if (state.password.isNotEmpty() && state.repeatPassword.isNotEmpty() && state.password != state.repeatPassword) {
            _uiState.update { it.copy(error = "Passwords don't match") }
        }
        return state.email.isNotEmpty() && state.password.isNotEmpty() && state.repeatPassword.isNotEmpty() && state.password == state.repeatPassword && state.username.isNotEmpty() && state.selectedCourse != null

    }

    private suspend fun checkSelectedCourse(userId: UUID, onLoginSuccess: () -> Unit) {
        val courseId = dataStorage.getCourseId()
        if (courseId == null) {
            val allCoursesResponse = getAllCourses.invoke(userId)
            if (allCoursesResponse.isSuccess) {
                val allCourses = allCoursesResponse.getOrNull()
                if (allCourses != null) {
                    checkForLoginWihCourse(_uiState.value)
                    Log.e("checkSelectedCourse", "allCourses = $allCourses")
                    listOfCourses = allCourses.map {
                        it.toPreview()
                    }
                    _uiState.update { it.copy(listOfCourses = listOfCourses) }
                    //showSelection
                }
            }
            //getAllCourses
            //showSelection
            // 1. Selected -> Save -> go to home
            // 2. Not selected -> Hide dialog -> Still on login
        } else {
            onLoginSuccess()
            //go to home, all right
        }
    }

}

fun CourseUI.toPreview(): CoursePreview {
    return CoursePreview(
        originalLanguage = originalLanguage,
        translateLanguage = translateLanguage,
        originalFlag = if (originalLanguage == Language.RUSSIAN) R.drawable.russia else R.drawable.germany,
        translatedFlag = if (originalLanguage == Language.RUSSIAN) R.drawable.russia else R.drawable.germany
    )
}

sealed class LoginIntent {
    data class UsernameChanged(val newUsername: String) : LoginIntent()
    data class EmailChanged(val newEmail: String) : LoginIntent()
    data class PasswordChanged(val newPassword: String) : LoginIntent()
    data class RepeatPasswordChanged(val newRepeatPassword: String) : LoginIntent()
    object SwitchAuth : LoginIntent()
    data class EnterClicked(val onLoginSuccess: () -> Unit) : LoginIntent()
    data class SelectCourse(val course: CoursePreview) : LoginIntent()
    object HideCourses : LoginIntent()
}