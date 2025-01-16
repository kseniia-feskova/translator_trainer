package com.presentation.ui.screens.start

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import com.presentation.model.CoursePreview
import com.presentation.usecases.IGetAccountUseCase
import com.presentation.usecases.course.IGetCourseUseCase
import com.presentation.usecases.auth.ILoginUseCase
import com.presentation.usecases.auth.IRegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class LoginViewModel(
    private val register: IRegisterUseCase,
    private val login: ILoginUseCase,
    private val getUser: IGetAccountUseCase,
    private val getCourse: IGetCourseUseCase,
    private val dataStorage: IDataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> onEmailChanged(intent.newEmail)
            is LoginIntent.EnterClicked -> onEnterClicked(intent.onLoginSuccess)
            is LoginIntent.PasswordChanged -> onPasswordChanged(intent.newPassword)
            is LoginIntent.RepeatPasswordChanged -> onRepeatPasswordChanged(intent.newRepeatPassword)
            LoginIntent.SwitchAuth -> onSwitchAuth()
            is LoginIntent.UsernameChanged -> onUserNameChanged(intent.newUsername)
            is LoginIntent.SelectCourse -> onCourseSelect(intent.course)
        }
    }

    private fun onEnterClicked(
        onLoginSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val state = _uiState.value
            val selectedCourse = state.selectedCourse
            val authResponse = if (state.isLogin) {
                login.invoke(state.email, state.username, state.password)
            } else {
                register(selectedCourse, state)
            }
            if (authResponse?.isSuccess == true) {
                val userId = authResponse.getOrNull()
                dataStorage.saveUserId(userId)
                if (selectedCourse != null) {
                    saveCourse(userId, selectedCourse)
                }
                onLoginSuccess()
            } else {
                Log.e("onEnterClicked", "Error = ${authResponse?.exceptionOrNull()?.message}")
                _uiState.update { it.copy(error = "${authResponse?.exceptionOrNull()?.message}") }
            }
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
        return state.email.isNotEmpty() && state.password.isNotEmpty() && state.username.isNotEmpty()
    }

    private fun checkForSignUp(state: LoginUIState): Boolean {
        if (state.password.isNotEmpty() && state.repeatPassword.isNotEmpty() && state.password != state.repeatPassword) {
            _uiState.update { it.copy(error = "Passwords don't match") }
        }
        return state.email.isNotEmpty() && state.password.isNotEmpty() && state.repeatPassword.isNotEmpty() && state.password == state.repeatPassword && state.username.isNotEmpty() && state.selectedCourse != null

    }

}

sealed class LoginIntent {
    data class UsernameChanged(val newUsername: String) : LoginIntent()
    data class EmailChanged(val newEmail: String) : LoginIntent()
    data class PasswordChanged(val newPassword: String) : LoginIntent()
    data class RepeatPasswordChanged(val newRepeatPassword: String) : LoginIntent()
    object SwitchAuth : LoginIntent()
    data class EnterClicked(val onLoginSuccess: () -> Unit) : LoginIntent()
    data class SelectCourse(val course: CoursePreview) : LoginIntent()
}