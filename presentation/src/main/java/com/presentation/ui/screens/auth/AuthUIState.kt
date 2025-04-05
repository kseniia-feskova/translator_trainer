package com.presentation.ui.screens.auth

import androidx.annotation.StringRes
import com.presentation.R

data class AuthUIState(
    val email: String = "",
    val password: String = "",
    val screenState: AuthScreenState = AuthScreenState.LOGIN,
    val error: AuthError? = null,
    val guestDialogVisible: Boolean = false,
    val isLoading: Boolean = false
)

sealed class AuthIntent {
    data class Auth(
        val goToCourses: () -> Unit,
        val goToHome: () -> Unit,
        val goToVerification: () -> Unit,
    ) : AuthIntent()

    object ChangeScreen : AuthIntent()
    data class OnEmailChanged(val login: String) : AuthIntent()
    data class OnPasswordChanged(val password: String) : AuthIntent()
    data class SaveAsGuest(
        val goToCourses: () -> Unit,
    ) : AuthIntent()
}

enum class AuthError(@StringRes val msg: Int) {
    EMPTY_FIELDS(R.string.empty_fields_error),
    USER_DOES_NOT_EXIST(R.string.user_does_not_exist_error),
    WRONG_PASSWORD(R.string.wrong_password_error),
    EMAIL_TAKEN(R.string.taken_email_error),
    INTERNET_CONNECTION_ERROR(R.string.internet_connection_error),
    DEFAULT(R.string.default_error)
}


enum class BaseError(@StringRes open val msg: Int) {
    INTERNET_CONNECTION_ERROR(R.string.internet_connection_error),
    DEFAULT(R.string.default_error)
}