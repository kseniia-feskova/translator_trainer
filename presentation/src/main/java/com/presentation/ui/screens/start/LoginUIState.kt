package com.presentation.ui.screens.start


data class LoginUIState(
    val isLogin: Boolean = true,
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val isValid: Boolean = false,
    val error: String? = null
)