package presentation.ui.screens.sets

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import presentation.usecases.course.ICoursesOnPrefsUseCases
import presentation.usecases.sets.IGetAllSetsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.model.LessonType

class SetsViewModel(
    private val getAllSets: IGetAllSetsUseCase,
    private val coursePrefs: ICoursesOnPrefsUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetsUIState())
    val uiState = _uiState.asStateFlow()

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val course = coursePrefs.getCourse()
            if (course != null) {
                Log.e("SetsViewModel", "reload, course = ${course.id} ")
                val response = getAllSets.invoke(course.id)
                val allWords = course.allWordsId
                if (response.isSuccess) {
                    val sets = response.getOrNull() ?: emptyList()
                    _uiState.update {
                        it.copy(
                            allWordsSet = allWords,
                            sets = if (sets.isNotEmpty() && sets[0].words.isEmpty()) emptyList() else sets,
                            loading = false
                        )
                    }
                } else {
                    handleError(response)
                }
            } else {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun isAllWordsSelected(setId: String): Boolean {
        Log.e("SetsViewModel", "SetId = $setId, all words = ${_uiState.value.allWordsSet}")
        return _uiState.value.allWordsSet == setId
    }

    private fun <T> handleError(response: Result<T>) {
        val error = response.exceptionOrNull()
        val errorMsg = when (error?.message) {
            "Failed to connect" -> SetsError.INTERNET_CONNECTION_ERROR
            else -> SetsError.DEFAULT
        }
        _uiState.update { it.copy(error = errorMsg, loading = false) }
    }

    fun createRandomLesson(
        navigateToLesson: (String, LessonType) -> Unit = { _, _ -> },
    ) {
        val type = LessonType.BUBBLE//LessonType.entries.random()
        val setId = _uiState.value.allWordsSet
        if (setId != null) {
            navigateToLesson(setId, type)
        }
    }
}