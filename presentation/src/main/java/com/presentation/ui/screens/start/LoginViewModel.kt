package com.presentation.ui.screens.start

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import com.presentation.usecases.auth.ILoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val login: ILoginUseCase,
    private val dataStore: IDataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.OnLoginChanged -> handleNewLogin(intent.login)
            is LoginIntent.OnPasswordChanged -> handleNewPassword(intent.password)
            LoginIntent.Login -> handleLogin()
        }
    }

    private fun handleNewLogin(login: String) {
        _uiState.update { it.copy(email = login, error = null) }
    }

    private fun handleNewPassword(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    private fun handleLogin() {
        val state = _uiState.value
        if (state.fieldsValid()) {
            viewModelScope.launch {
                val response = login.invoke(state.email, state.email, state.password)
                if (response.isSuccess) {
                    val userId = response.getOrNull()
                    if (userId != null) {
                        dataStore.saveUserId(userId)
                        Log.e("NewLoginVM", "Login success")
                    } else {
                        _uiState.update { it.copy(error = LoginError.USER_DOES_NOT_EXIST) }
                    }
                } else {
                    val error = response.exceptionOrNull()
                    val errorMsg = when (error?.message) {
                        "Wrong password" -> LoginError.WRONG_PASSWORD
                        "User does not exist" -> LoginError.USER_DOES_NOT_EXIST
                        else -> LoginError.DEFAULT
                    }
                    _uiState.update { it.copy(error = errorMsg) }
                }
            }
        } else {
            _uiState.update { it.copy(error = LoginError.EMPTY_FIELDS) }
        }
    }

    private fun LoginUIState.fieldsValid() = email.isNotEmpty() && password.isNotEmpty()

}