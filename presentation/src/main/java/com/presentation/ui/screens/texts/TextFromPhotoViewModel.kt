package com.presentation.ui.screens.texts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.model.CourseUI
import com.presentation.usecases.ITranslateWordUseCase
import com.presentation.usecases.course.ICoursesOnPrefsUseCases
import com.presentation.usecases.words.IAddWordUseCase
import com.presentation.utils.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TextFromPhotoUI(
    val originalLanguage: Language = Language.RUSSIAN,
    val resLanguage: Language = Language.GERMAN,
    val original: String = "",
    val translate: String = "",
    val selectedWords: List<String> = emptyList(),
    val allWords: List<String> = emptyList(),
    val loading: Boolean = false
)

class TextFromPhotoViewModel(
    private val translateWord: ITranslateWordUseCase,
    private val coursesPrefs: ICoursesOnPrefsUseCases,
    private val addWordUseCase: IAddWordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TextFromPhotoUI())
    val uiState = _uiState.asStateFlow()
    private var course: CourseUI? = null

    init {
        viewModelScope.launch {
            course = coursesPrefs.getCourse()
            course?.let { course ->
                _uiState.update {
                    it.copy(
                        originalLanguage = course.originalLanguage,
                        resLanguage = course.translateLanguage
                    )
                }
            }
        }
    }

    fun translateText(text: String) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            Log.e("TextFromPhotoVM", "Text = $text")
            val formated = text.replace('\n', ' ')
            val translatedText = translateWord.invoke(
                formated,
                _uiState.value.originalLanguage,
                _uiState.value.resLanguage
            )
            _uiState.update {
                it.copy(
                    loading = false,
                    translate = translatedText,
                    original = text,
                    allWords = text.split(Regex("\\s+|[,.!?;:()]")).filter { it.isNotBlank() }
                )
            }
        }
    }

    fun changeLanguages(selected: Language) {
        if (_uiState.value.originalLanguage != selected) {
            _uiState.update {
                it.copy(
                    resLanguage = it.originalLanguage,
                    originalLanguage = it.resLanguage
                )
            }
        }
    }

    fun onSelect(text: String) {
        val selected = _uiState.value.selectedWords.toMutableList()
        if (selected.contains(text)) {
            selected.remove(text)
        } else {
            selected.add(text)
        }
        _uiState.update {
            it.copy(
                selectedWords = selected.toList()
            )
        }

    }

    fun saveWords() {
        val list = _uiState.value.selectedWords
        list.map {
            viewModelScope.launch {
                val translatedText = translateWord.invoke(
                    it,
                    _uiState.value.originalLanguage,
                    _uiState.value.resLanguage
                )
                val response = addWordUseCase.invoke(it, translatedText)
//                if (response.isSuccess) {
//                    val savedWord = response.getOrNull()
//                    if (savedWord != null) {
//                        _uiState.update { it.copy(loading = false, selectedWords = ) }
//                    } else {
//                        _uiState.update { it.copy(loading = false) }
//                    }
//                } else {
//                   // handleError(response)
//                }
            }
        }
    }
}