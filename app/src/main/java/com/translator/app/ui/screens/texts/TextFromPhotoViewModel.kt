package com.translator.app.ui.screens.texts

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.model.CourseUI
import presentation.model.WordSelection
import domain.usecases.ITranslateWordUseCase
import domain.usecases.course.ICoursesOnPrefsUseCases
import domain.usecases.words.IAddWordUseCase
import mapper.toData
import mapper.toUI
import presentation.Language
import presentation.TextRecognizer
import java.util.UUID

data class TextFromPhotoUI(
    val originalLanguage: Language? = null,
    val resLanguage: Language? = null,
    val original: String = "",
    val translate: String = "",
    val selectedWords: List<WordSelection> = emptyList(),
    val loading: Boolean = false
)

//TODO: create common module for language selection resolve. it will be reused on the HomeScreen
class TextFromPhotoViewModel(
    private val translateWord: ITranslateWordUseCase,
    private val coursesPrefs: ICoursesOnPrefsUseCases,
    private val addWordUseCase: IAddWordUseCase,
    private val textRecognizer: TextRecognizer
) : ViewModel() {

    private val _uiState = MutableStateFlow(TextFromPhotoUI())
    val uiState = _uiState.asStateFlow()
    private var course: CourseUI? = null

    init {
        viewModelScope.launch {
            course = coursesPrefs.getCourse()?.toUI()
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

    private fun translateText(text: String) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            Log.e("TextFromPhotoVM", "Text = $text")
            val formated = text.replace('\n', ' ')
            val origin = _uiState.value.originalLanguage
            val res = _uiState.value.resLanguage
            if (origin != null && res != null) {
                translateWord.invoke(
                    text = formated,
                    originalLanguage = origin.toData(),
                    resLanguage = res.toData(),
                    onSuccess = { translated ->
                        _uiState.update {
                            it.copy(
                                loading = false,
                                translate = translated,
                                original = text
                            )
                        }
                    },
                    onError = {
                        Log.e("TextFromPhoto", "Translate Error $it")
                    }
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
            if (_uiState.value.original.isNotEmpty()) {
                translateText(_uiState.value.original)
            }
        }
    }

    fun onSelect(text: String) {
        if (_uiState.value.selectedWords.none { it.originalText == text }) {
            val originalLanguage =
                if (_uiState.value.originalLanguage == course?.originalLanguage) {
                    _uiState.value.originalLanguage
                } else {
                    _uiState.value.resLanguage
                }
            val resLanguage = if (_uiState.value.originalLanguage != course?.originalLanguage) {
                _uiState.value.originalLanguage
            } else {
                _uiState.value.resLanguage
            }
            if (originalLanguage != null && resLanguage != null) {
                viewModelScope.launch {
                    translateWord.invoke(
                        text,
                        originalLanguage = originalLanguage.toData(),
                        resLanguage = resLanguage.toData(),
                        onSuccess = {
                            val selected = _uiState.value.selectedWords.toMutableList()
                            selected.add(
                                WordSelection(
                                    UUID.randomUUID().toString(),
                                    originalText = text,
                                    resText = it
                                )
                            )
                            _uiState.update {
                                it.copy(
                                    selectedWords = selected.toList()
                                )
                            }

                        },
                        onError = {
                            Log.e("TextFromPhotoVM", "onSelect error of translation")
                        }
                    )
                }
            }
        }
    }

    fun saveWord(word: WordSelection) {
        viewModelScope.launch {
            val response = if (_uiState.value.originalLanguage == course?.originalLanguage) {
                addWordUseCase.invoke(word.originalText, word.resText)
            } else {
                addWordUseCase.invoke(word.resText, word.originalText)
            }
            if (response.isSuccess) {
                val savedWord = response.getOrNull()?.toUI()
                if (savedWord != null) {
                    val selected = _uiState.value.selectedWords.toMutableList()
                    _uiState.update {
                        it.copy(
                            selectedWords = selected.map {
                                if (it == word) {
                                    it.copy(isSaved = true)
                                } else it
                            }
                        )
                    }
                }
            }
        }
    }

    fun recognizeText(bitmap: Bitmap) {
        textRecognizer.recognizeText(
            bitmap,
            onSuccess = { text ->
                _uiState.update { it.copy(selectedWords = emptyList()) }
                translateText(text)
            },
            onError = { Log.e("Translate", "Ошибка распознавания") }
        )
    }
}