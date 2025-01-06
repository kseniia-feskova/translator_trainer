package com.presentation.ui.screens.start

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import com.presentation.usecases.auth.ILoginUseCase
import com.presentation.usecases.auth.IRegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val register: IRegisterUseCase,
    private val login: ILoginUseCase,
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
        }
    }

    private fun onEnterClicked(
        onLoginSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val state = _uiState.value
            val response = if (state.isLogin) {
                login.invoke(state.email, state.password)
            } else register.invoke(state.email, state.password)
            if (response.isSuccess) {
                val id = response.getOrNull()
                dataStorage.saveUserId(id)
                onLoginSuccess()
            } else {
                Log.e("onEnterClicked", "Error = ${response.exceptionOrNull()?.message}")
                _uiState.update {
                    it.copy(
                        error = "${response.exceptionOrNull()?.message}"
                    )
                }
            }
        }
    }

    private fun onEmailChanged(newEmail: String) {
        _uiState.update {
            it.copy(
                email = newEmail, error = null
            )
        }
        validate()
    }

    private fun onPasswordChanged(newPassword: String) {
        Log.e("onPasswordChanged", "newPassword = $newPassword")
        _uiState.update {
            it.copy(
                password = newPassword, error = null
            )
        }
        validate()
    }

    private fun onRepeatPasswordChanged(newPassword: String) {
        _uiState.update {
            it.copy(
                repeatPassword = newPassword, error = null
            )
        }
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

    private fun validate() {
        _uiState.update {
            it.copy(
                isValid = if (it.isLogin) checkForLogin(it) else checkForSignUp(it)
            )
        }
    }

    private fun checkForLogin(state: LoginUIState): Boolean {
        return state.email.isNotEmpty() && state.password.isNotEmpty()
    }

    private fun checkForSignUp(state: LoginUIState): Boolean {
        if (state.password.isNotEmpty() && state.repeatPassword.isNotEmpty() && state.password != state.repeatPassword) {
            _uiState.update {
                it.copy(error = "Passwords don't match")
            }
        }
        return state.email.isNotEmpty() && state.password.isNotEmpty() && state.repeatPassword.isNotEmpty() && state.password == state.repeatPassword

    }

}

sealed class LoginIntent {
    data class EmailChanged(val newEmail: String) : LoginIntent()
    data class PasswordChanged(val newPassword: String) : LoginIntent()
    data class RepeatPasswordChanged(val newRepeatPassword: String) : LoginIntent()
    object SwitchAuth : LoginIntent()
    data class EnterClicked(val onLoginSuccess: () -> Unit) : LoginIntent()
}