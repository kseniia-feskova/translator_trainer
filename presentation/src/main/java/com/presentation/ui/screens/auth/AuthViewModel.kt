package com.presentation.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import com.presentation.usecases.auth.ILoginUseCase
import com.presentation.usecases.auth.IRegisterUseCase
import com.presentation.usecases.course.IGetAllCoursesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class AuthViewModel(
    private val login: ILoginUseCase,
    private val register: IRegisterUseCase,
    private val getCourses: IGetAllCoursesUseCase,
    private val dataStore: IDataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUIState())
    val uiState = _uiState.asStateFlow()

    fun handleIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.OnEmailChanged -> handleNewLogin(intent.login)
            is AuthIntent.OnPasswordChanged -> handleNewPassword(intent.password)
            is AuthIntent.Auth -> handleAuth(intent.goToCourses, intent.goToHome)
            AuthIntent.ChangeScreen -> handleScreenChange()
        }
    }

    private fun handleNewLogin(login: String) {
        _uiState.update { it.copy(email = login, error = null) }
    }

    private fun handleNewPassword(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    private fun handleAuth(
        goToCourses: () -> Unit,
        goToHome: () -> Unit
    ) {
        val state = _uiState.value
        if (state.fieldsValid()) {
            when (state.screenState) {
                AuthScreenState.LOGIN -> login(state, goToCourses, goToHome)
                AuthScreenState.REGISTER -> register(state, goToCourses)
            }
        } else {
            _uiState.update { it.copy(error = AuthError.EMPTY_FIELDS) }
        }
    }

    private fun login(
        state: AuthUIState,
        goToCourses: () -> Unit,
        goToHome: () -> Unit
    ) {
        viewModelScope.launch {
            val response = login.invoke(state.email, state.email, state.password)
            if (!response.isSuccess) {
                handleError(response)
                return@launch
            }
            val userId = response.getOrNull()
            if (userId != null) {
                dataStore.saveUserId(userId)
                checkCourses(userId, goToCourses, goToHome)
                Log.e("NewLoginVM", "Login success")
            } else {
                _uiState.update { it.copy(error = AuthError.USER_DOES_NOT_EXIST) }
            }
        }
    }

    private fun checkCourses(userId: UUID, goToCourses: () -> Unit, goToHome: () -> Unit) {
        viewModelScope.launch {
            val response = getCourses.invoke(userId)
            if (!response.isSuccess) {
                handleError(response)
                return@launch
            }
            val courses = response.getOrNull() ?: return@launch
            if (courses.size != 1) {
                dataStore.saveCourses(courses)
                goToCourses()
            } else {
                dataStore.saveCourse(courses.first())
                goToHome()
            }
        }
    }

    private fun register(
        state: AuthUIState,
        goToCourses: () -> Unit,
    ) {
        viewModelScope.launch {
            val response = register.invoke(state.email, state.email, state.password)
            if (!response.isSuccess) {
                handleError(response)
                return@launch
            }
            val userId = response.getOrNull()
            if (userId != null) {
                dataStore.saveUserId(userId)
                goToCourses()
                Log.e("NewLoginVM", "Register success")
            } else {
                _uiState.update { it.copy(error = AuthError.USER_DOES_NOT_EXIST) }
            }
        }
    }

    private fun handleScreenChange() {
        _uiState.update { it.copy(screenState = if (it.screenState == AuthScreenState.LOGIN) AuthScreenState.REGISTER else AuthScreenState.LOGIN) }
    }

    private fun AuthUIState.fieldsValid() = email.isNotEmpty() && password.isNotEmpty()

    private fun <T> handleError(response: Result<T>) {
        val error = response.exceptionOrNull()
        val errorMsg = when (error?.message) {
            "Wrong password" -> AuthError.WRONG_PASSWORD
            "User does not exist" -> AuthError.USER_DOES_NOT_EXIST
            "User already exists" -> AuthError.EMAIL_TAKEN
            else -> AuthError.DEFAULT
        }
        _uiState.update { it.copy(error = errorMsg) }
    }
}