package presentation.ui.screens.select_course

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import presentation.model.CourseUI
import com.presentation.usecases.auth.ILogoutUseCase
import com.presentation.usecases.course.IAddCourseUseCase
import domain.translate.ITranslateModelProvider
import presentation.usecases.course.ICoursesOnPrefsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mapper.toData

class SelectCourseViewModel(
    private val getCourses: ICoursesOnPrefsUseCases,
    private val logout: ILogoutUseCase,
    private val addCourse: IAddCourseUseCase,
    private val translatorProvider: ITranslateModelProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectCourseUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val savedCourses = getCourses.getAll()
            _uiState.update {
                it.copy(
                    courses = savedCourses,
                    selectedCourse = savedCourses.firstOrNull()
                )
            }
        }
    }

    fun handleIntent(intent: SelectCourseIntent) {
        when (intent) {
            is SelectCourseIntent.OnContinueClicked -> handleContinue(intent.goToHome)
            is SelectCourseIntent.OnBackClicked -> handleBackClicked(intent.navigateUp)
            is SelectCourseIntent.OnCourseSelected -> handleSelectedCourse(intent.courseUI)
        }
    }

    private fun handleBackClicked(navigateUp: () -> Unit) {
        viewModelScope.launch {
            logout.invoke()
            navigateUp()
        }
    }

    private fun handleContinue(goToHome: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.selectedCourse == null) {
                Log.e("handleContinue", "Selected course is null")
                return@launch
            }
            val response = addCourse.invoke(state.selectedCourse, true)
            if (response.isSuccess) {
                translatorProvider.downloadModel(
                    state.selectedCourse.originalLanguage.toData(),
                    state.selectedCourse.translateLanguage.toData()
                ) {
                    Log.e("SelectCourseVM", "Download model error $it")
                }
                goToHome()
            } else {
                Log.e("SelectCourseVM", "save course error = ${response}")
                //handleError()
            }
        }
    }

    private fun handleSelectedCourse(course: CourseUI) {
        _uiState.update { it.copy(selectedCourse = course) }
    }

}