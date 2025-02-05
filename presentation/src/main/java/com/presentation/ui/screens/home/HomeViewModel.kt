package com.presentation.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import com.presentation.model.CourseUI
import com.presentation.usecases.ITranslateWordUseCase
import com.presentation.usecases.words.IAddWordUseCase
import com.presentation.usecases.words.IGetWordByOriginal
import com.presentation.usecases.words.IGetWordByTranslated
import com.presentation.utils.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class HomeViewModel(
    private val findWordByOrigin: IGetWordByOriginal,
    private val findWordByTranslate: IGetWordByTranslated,
    private val translateWord: ITranslateWordUseCase,
    private val addWordUseCase: IAddWordUseCase,
    private val prefs: IDataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState = _uiState.asStateFlow()
    private var course: CourseUI? = null

    init {
        viewModelScope.launch {
            course = prefs.getCourse()
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
        }
    }

    private fun translateInput() {
        if (_uiState.value.inputText.isNotEmpty()) {
            _uiState.update { it.copy(loading = true) }
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
                val savedWord = response.getOrNull()
                if (savedWord != null) {
                    _uiState.update { it.copy(loading = false, isWordSaved = true) }
                } else {
                    Log.e(TAG, "savedWord is null")
                }
            } else {
                Log.e(TAG, "SaveWord response is failed")
            }
        }
    }

    private fun translateText(text: String) {
        viewModelScope.launch {
            val translatedText = translateWord.invoke(
                text,
                _uiState.value.originalLanguage,
                _uiState.value.resLanguage
            )
            _uiState.update {
                it.copy(
                    translatedText = translatedText,
                    showGlow = true,
                    loading = false
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
                    translatedText = if (it.translatedText.isNotEmpty()) "" else it.translatedText
                )
            }
        }
    }

    private fun translateFromOrigin(origin: String) {
        viewModelScope.launch {
            val wordResult = findWordByOrigin.invoke(origin)
            if (wordResult.isSuccess) {
                val word = wordResult.getOrNull()
                if (word == null) {
                    translateText(origin)
                } else {
                    _uiState.update {
                        it.copy(
                            loading = false,
                            translatedText = word.resText,
                            isWordSaved = true
                        )
                    }
                }
            } else {
                val error = wordResult.exceptionOrNull()?.message
                if (error == NEW_WORD) {
                    translateText(origin)
                } else {
                    Log.e("translateFromTranslated", "Error = $error")
                }
            }
        }
    }

    private fun translateFromTranslated(translated: String) {
        viewModelScope.launch {
            val wordResult = findWordByTranslate.invoke(translated)
            if (wordResult.isSuccess) {
                val word = wordResult.getOrNull()
                if (word == null) {
                    translateText(translated)
                } else {
                    _uiState.update {
                        it.copy(
                            loading = false,
                            translatedText = word.originalText,
                            isWordSaved = true
                        )
                    }
                }
            } else {
                val error = wordResult.exceptionOrNull()?.message
                if (error == NEW_WORD) {
                    translateText(translated)
                } else {
                    Log.e("translateFromTranslated", "Error = $error")
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
                    translatedText = "",
                    isWordSaved = false
                )
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
        private const val NEW_WORD = "Word does not exist"
    }
}
