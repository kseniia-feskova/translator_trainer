package com.presentation.ui.screens.start

import androidx.annotation.StringRes
import com.presentation.R

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val error: LoginError? = null
)

sealed class LoginIntent {
    object Login : LoginIntent()
    data class OnLoginChanged(val login: String) : LoginIntent()
    data class OnPasswordChanged(val password: String) : LoginIntent()
}

enum class LoginError(@StringRes val msg: Int) {
    EMPTY_FIELDS(R.string.empty_fields_error),
    USER_DOES_NOT_EXIST(R.string.user_does_not_exist_error),
    WRONG_PASSWORD(R.string.wrong_password_error),
    DEFAULT(R.string.default_error)
}
