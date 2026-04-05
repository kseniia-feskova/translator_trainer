package com.translator.app.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.usecases.ITranslateWordUseCase
import domain.usecases.course.ICoursesOnPrefsUseCases
import domain.usecases.words.IAddWordUseCase
import domain.usecases.words.IGetWordByOriginal
import domain.usecases.words.IGetWordByTranslated
import presentation.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mapper.toData
import mapper.toUI
import presentation.model.CourseUI


class HomeViewModel(
    private val findWordByOrigin: IGetWordByOriginal,
    private val findWordByTranslate: IGetWordByTranslated,
    private val translateWord: ITranslateWordUseCase,
    private val addWordUseCase: IAddWordUseCase,
    private val coursesPrefs: ICoursesOnPrefsUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState = _uiState.asStateFlow()
    private var course: CourseUI? = null

    override fun onCleared() {
        translateWord.closeResources()
        super.onCleared()
    }

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

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.InputText -> onTextChange(intent.text)
            is HomeIntent.EnterText -> translateInput()
            is HomeIntent.SaveWord -> saveWord()
            is HomeIntent.ChangeLanguages -> changeLanguages(intent.selectedLang)
            HomeIntent.HideLimitsError -> _uiState.update { it.copy(limitsError = false) }
            is HomeIntent.AlterTranslateSave -> saveAlterTranslate(intent.alterText)
        }
    }

    private fun translateInput() {
        if (_uiState.value.inputText.isNotEmpty()) {
            _uiState.update { it.copy(loading = true, altTranslates = null) }
            if (_uiState.value.originalLanguage == course?.originalLanguage) {
                translateFromOrigin(_uiState.value.inputText)
            } else {
                translateFromTranslated(_uiState.value.inputText)
            }
        }
    }

    private fun saveWord() {
        viewModelScope.launch {
            val word = _uiState.value
            _uiState.update { it.copy(loading = true) }
            val response = if (word.originalLanguage == course?.originalLanguage) {
                addWordUseCase.invoke(word.inputText, word.translatedText)
            } else {
                addWordUseCase.invoke(word.translatedText, word.inputText)
            }
            if (response.isSuccess) {
                val savedWord = response.getOrNull()?.toUI()
                if (savedWord != null) {
                    _uiState.update { it.copy(loading = false, isWordSaved = true, error = null) }
                } else {
                    _uiState.update { it.copy(loading = false, error = HomeError.DEFAULT) }
                }
            } else {
                handleError(response)
            }
        }
    }

    private fun saveAlterTranslate(alterText: String) {
        viewModelScope.launch {
            val word = _uiState.value
            _uiState.update { it.copy(loading = true) }
            val response = if (word.originalLanguage == course?.originalLanguage) {
                addWordUseCase.invoke(word.inputText, alterText)
            } else {
                addWordUseCase.invoke(alterText, word.inputText)
            }
            if (response.isSuccess) {
                val savedWord = response.getOrNull()?.toUI()
                if (savedWord != null) {
                    _uiState.update { it.copy(loading = false, isWordSaved = true, error = null) }
                } else {
                    _uiState.update { it.copy(loading = false, error = HomeError.DEFAULT) }
                }
            } else {
                handleError(response)
            }
        }
    }

    private fun translateText(text: String) {
        viewModelScope.launch {
            val origin = _uiState.value.originalLanguage
            val res = _uiState.value.resLanguage
            if (origin != null && res != null) {
                translateWord.invoke(
                    text = text,
                    originalLanguage = origin.toData(),
                    resLanguage = res.toData(),
                    { translated ->
                        _uiState.update {
                            it.copy(
                                translatedText = translated,
                                altTranslates = emptyList(),
                                showGlow = true,
                                loading = false,
                                error = null
                            )
                        }
                    },
                    onError = { exception ->
                        _uiState.update {
                            it.copy(
                                showGlow = true,
                                loading = false,
                                error = HomeError.TRANSLATION_ERROR
                            )
                        }
                        Log.e(
                            "HomeViewModel", "Error $exception"
                        )
                    }
                )
            }
        }
    }

    private fun onTextChange(inputText: String) {
        if (inputText != _uiState.value.inputText) {
            _uiState.update {
                it.copy(
                    isWordSaved = false,
                    inputText = inputText,
                    error = null,
                    altTranslates = null,
                    translatedText = if (it.translatedText.isNotEmpty()) "" else it.translatedText
                )
            }
        }
    }

    private fun translateFromOrigin(origin: String) {
        viewModelScope.launch {
            val wordResult = findWordByOrigin.invoke(origin)
            if (wordResult.isSuccess) {
                val word = wordResult.getOrNull()?.toUI()
                if (word == null) {
                    translateText(origin)
                } else {
                    _uiState.update {
                        it.copy(
                            loading = false,
                            translatedText = word.resText,
                            isWordSaved = true,
                            error = null
                        )
                    }
                }
            } else {
                val error = wordResult.exceptionOrNull()?.message
                if (error == NEW_WORD || error == "Failed to connect") {
                    translateText(origin)
                } else {
                    handleError(wordResult)
                }
            }
        }
    }

    private fun translateFromTranslated(translated: String) {
        viewModelScope.launch {
            val wordResult = findWordByTranslate.invoke(translated)
            if (wordResult.isSuccess) {
                val word = wordResult.getOrNull()?.toUI()
                if (word == null) {
                    translateText(translated)
                } else {
                    _uiState.update {
                        it.copy(
                            loading = false,
                            translatedText = word.originalText,
                            isWordSaved = true,
                            error = null
                        )
                    }
                }
            } else {
                val error = wordResult.exceptionOrNull()?.message
                if (error == NEW_WORD || error == "Failed to connect") {
                    translateText(translated)
                } else {
                    handleError(wordResult)
                }
            }
        }
    }

    private fun changeLanguages(selected: Language) {
        if (_uiState.value.originalLanguage != selected) {
            _uiState.update {
                it.copy(
                    resLanguage = it.originalLanguage,
                    originalLanguage = it.resLanguage,
                    inputText = "",
                    altTranslates = null,
                    translatedText = "",
                    isWordSaved = false,
                    error = null
                )
            }
        }
    }

    private fun <T> handleError(response: Result<T>) {
        val error = response.exceptionOrNull()
        val errorMsg = when (error?.message) {
            "Failed to connect" -> HomeError.INTERNET_CONNECTION_ERROR

            "Guest limit" -> {
                _uiState.update { it.copy(limitsError = true, loading = false) }
                null
            }

            else -> HomeError.DEFAULT
        }
        if (errorMsg != null) {
            _uiState.update { it.copy(error = errorMsg, loading = false) }
        }
    }

    companion object {
        private const val NEW_WORD = "Word does not exist"
    }
}
