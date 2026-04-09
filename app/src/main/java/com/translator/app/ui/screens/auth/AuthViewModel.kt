package com.translator.app.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.translate.Language
import data.translate.languageOf
import domain.translate.ITranslateModelProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mapper.toData
import presentation.auth.AuthInteractor
import presentation.auth.AuthResult
import presentation.auth.BaseAuthError
import presentation.model.FirebaseUser

class AuthViewModel(
    private val interactor: AuthInteractor,
    private val translatorProvider: ITranslateModelProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUIState())
    val uiState = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<AuthNav>()
    val navigation = _navigation.asSharedFlow()

    private fun AuthUIState.fieldsValid() = email.isNotEmpty() && password.isNotEmpty()

    fun handleIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.OnEmailChanged -> handleNewLogin(intent.login)
            is AuthIntent.OnPasswordChanged -> handleNewPassword(intent.password)
            is AuthIntent.Auth -> handleAuth()
            is AuthIntent.SaveAsGuest -> handleGuest()
            AuthIntent.ChangeScreen -> handleScreenChange()
            is AuthIntent.GoogleSign -> handleGoogleSign(intent.firebaseUser)
        }
    }

    private fun handleNewLogin(login: String) {
        _uiState.update { it.copy(email = login, error = null) }
    }

    private fun handleNewPassword(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    private fun handleAuth() {
        val state = _uiState.value
        if (state.fieldsValid()) {
            _uiState.update { it.copy(isLoading = true) }
            when (state.screenState) {
                AuthScreenState.LOGIN -> login(state)
                AuthScreenState.REGISTER -> register(state)
            }
        } else {
            _uiState.update { it.copy(error = AuthError.EMPTY_FIELDS) }
        }
        _uiState.update { it.copy(isLoading = false) }
    }

    private suspend fun handleAuthResult(result: AuthResult) {
        when (result) {

            AuthResult.Success, is AuthResult.NeedsCourseSelection -> {
                _navigation.emit(AuthNav.ToCourses)
                _uiState.update { it.copy(isLoading = false) }
            }

            AuthResult.NeedsVerification -> {
                _navigation.emit(AuthNav.ToVerification)
                _uiState.update { it.copy(isLoading = false) }
            }

            is AuthResult.Error -> {
                handleError(result.error)
            }

            is AuthResult.GoToHome -> {
                translatorProvider.downloadModel(
                    languageOf(result.course.sourceLanguage) ?: Language.AUTO,
                    languageOf(result.course.targetLanguage) ?: Language.AUTO
                ) {
                    Log.e("AuthVM", "Download model error $it")
                }
                _navigation.emit(AuthNav.ToHome)
                _uiState.update { it.copy(isLoading = false) }
            }

        }
    }

    private fun login(state: AuthUIState) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = interactor.login(state.email, state.password)
            handleAuthResult(result)
        }
    }

    private fun register(state: AuthUIState) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = interactor.register(state.email, state.password)
            handleAuthResult(result)
        }
    }

    private fun handleGoogleSign(firebaseUser: FirebaseUser?) {
        if (firebaseUser == null) {
            Log.e("handleGoogleSign", "user is null, need to handle error")
            return
        }
        viewModelScope.launch {
            handleAuthResult(interactor.loginWithGoogle(firebaseUser.toData()))
        }
    }

    private fun handleGuest() {
        viewModelScope.launch {
            handleAuthResult(interactor.continueAsGuest())
        }
    }

    private fun handleScreenChange() {
        _uiState.update {
            it.copy(
                screenState = if (it.screenState == AuthScreenState.LOGIN) AuthScreenState.REGISTER else AuthScreenState.LOGIN,
                error = null
            )
        }
    }

    private fun handleError(error: BaseAuthError) {
        val message = when (error) {
            BaseAuthError.USER_DOES_NOT_EXIST -> AuthError.USER_DOES_NOT_EXIST
            BaseAuthError.WRONG_PASSWORD -> AuthError.WRONG_PASSWORD
            BaseAuthError.EMAIL_TAKEN -> AuthError.EMAIL_TAKEN
            BaseAuthError.INTERNET_CONNECTION_ERROR -> AuthError.INTERNET_CONNECTION_ERROR
            else -> AuthError.DEFAULT
        }
        _uiState.update { it.copy(error = message, isLoading = false) }
    }
}

sealed class AuthNav {
    object ToCourses : AuthNav()
    object ToHome : AuthNav()
    object ToVerification : AuthNav()
}