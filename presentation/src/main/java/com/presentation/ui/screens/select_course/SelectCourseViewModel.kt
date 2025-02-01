package com.presentation.ui.screens.select_course

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import com.presentation.model.CourseUI
import com.presentation.test.dummyCourses
import com.presentation.usecases.auth.ILogoutUseCase
import com.presentation.usecases.course.IAddCourseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class SelectCourseViewModel(
    private val dataStore: IDataStoreManager,
    private val logout: ILogoutUseCase,
    private val addCourse: IAddCourseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectCourseUIState())
    val uiState = _uiState.asStateFlow()


    init {
        val savedCourses = dataStore.getCourses().ifEmpty { dummyCourses }
        _uiState.update {
            it.copy(
                courses = savedCourses,
                selectedCourse = savedCourses.firstOrNull()
            )
        }
    }

    fun handleIntent(intent: SelectCourseIntent) {
        when (intent) {
            is SelectCourseIntent.OnContinueClicked -> handleContinue(intent.goToHome)
            SelectCourseIntent.OnBackClicked -> handleBackClicked()
            is SelectCourseIntent.OnCourseSelected -> handleSelectedCourse(intent.courseUI)
        }
    }

    private fun handleBackClicked() {
        viewModelScope.launch {
            logout.invoke()
            dataStore.saveUserId(null)
        }
    }

    private fun handleContinue(goToHome: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.selectedCourse == null) {
                Log.e("handleContinue", "Selected course is null")
                return@launch
            }
            val userId = dataStore.listenUserId().firstOrNull()
            if (userId == null) {
                Log.e("handleContinue", "UserId is null")
                return@launch
            }
            if (state.courses == dummyCourses) {
                createNewCourse(userId, state.selectedCourse)
            } else {
                dataStore.saveCourseId(state.selectedCourse.id)
            }
            goToHome()
        }
    }

    private suspend fun createNewCourse(userId: UUID, course: CourseUI) {
        val addCourseResponse = addCourse.invoke(userId, course)
        if (addCourseResponse.isSuccess) {
            val savedCourse = addCourseResponse.getOrNull()
            if (savedCourse != null) {
                dataStore.saveCourseId(savedCourse.id)
            } else {
                Log.e("createNewCourse", "savedCourse is null")
            }
        } else {
            Log.e("createNewCourse", "${addCourseResponse.exceptionOrNull()?.message}")
        }
    }

    private fun handleSelectedCourse(course: CourseUI) {
        _uiState.update { it.copy(selectedCourse = course) }
    }

}