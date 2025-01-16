package com.presentation.ui.screens.start

import com.presentation.model.CoursePreview


data class LoginUIState(
    val isLogin: Boolean = true,
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val selectedCourse: CoursePreview? = null,
    val isValid: Boolean = false,
    val error: String? = null
)