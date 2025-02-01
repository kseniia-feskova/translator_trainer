package com.presentation.ui.screens.select_course

import com.presentation.model.CourseUI

data class SelectCourseUIState(
    val selectedCourse: CourseUI? = null,
    val courses: List<CourseUI> = emptyList()
)

sealed class SelectCourseIntent {
    data class OnCourseSelected(val courseUI: CourseUI) : SelectCourseIntent()
    data class OnContinueClicked(val goToHome: () -> Unit) : SelectCourseIntent()
    object OnBackClicked : SelectCourseIntent()
}