package com.presentation.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import com.presentation.usecases.ITranslateWordUseCase
import com.presentation.usecases.words.IAddWordUseCase
import com.presentation.usecases.words.IFindWordByOriginUseCase
import com.presentation.usecases.words.IGetWordByTranslated
import com.presentation.utils.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class HomeViewModel(
    private val findWordByOrigin: IFindWordByOriginUseCase,
    private val findWordByTranslate: IGetWordByTranslated,
    private val translateWord: ITranslateWordUseCase,
    private val addWordUseCase: IAddWordUseCase,
    private val prefs: IDataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState = _uiState.asStateFlow()
    private var setId: Int? = null

    private var originalLanguage: Language? = null
    private var translatedLanguage: Language? = null

    init {
        viewModelScope.launch {
            originalLanguage = prefs.getOriginalLanguage()
            translatedLanguage = prefs.getResultLanguage()
            originalLanguage?.let { original ->
                _uiState.update { it.copy(originalLanguage = original) }
            }
            translatedLanguage?.let { translated ->
                _uiState.update { it.copy(resLanguage = translated) }
            }
            prefs.listenSelectedSetId().collectLatest { setId = it }
        }
    }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.InputText -> {
                onTextChange(intent.text)
            }

            is HomeIntent.EnterText -> {
                _uiState.update { it.copy(loading = true) }
                if (_uiState.value.originalLanguage == originalLanguage) {
                    translateFromOrigin(intent.text)
                } else {
                    translateFromTranslated(intent.text)
                }
            }

            is HomeIntent.SaveWord -> {
                viewModelScope.launch {
                    val word = _uiState.value
                    _uiState.update { it.copy(loading = true) }
                    val response = if (word.originalLanguage == originalLanguage) {
                        addWordUseCase.invoke(setId, word.inputText, word.translatedText)
                    } else {
                        addWordUseCase.invoke(setId, word.translatedText, word.inputText)
                    }
                    if (response.isSuccess) {
                        val savedWord = response.getOrNull()
                        if (savedWord != null) {
                            _uiState.update { it.copy(loading = false, savedWord = true) }
                        } else {
                            Log.e(TAG, "savedWord is null")
                        }
                    } else {
                        Log.e(TAG, "SaveWord response is failed")
                    }
                }
            }

            is HomeIntent.ChangeLanguages -> {
                changeLanguages()
            }
        }
    }

    private fun translateText(
        text: String,
        originalLanguage: Language,
        resLanguage: Language
    ) {
        viewModelScope.launch {
            val translatedText = translateWord.invoke(text, originalLanguage, resLanguage)
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
                    savedWord = false,
                    inputText = inputText,
                    translatedText = if (it.translatedText.isNotEmpty()) "" else it.translatedText
                )
            }
        }
    }

    private fun translateFromOrigin(origin: String) {
        viewModelScope.launch {
            val word = findWordByOrigin.invoke(origin)
            if (word == null) {
                translateText(
                    origin,
                    _uiState.value.originalLanguage,
                    _uiState.value.resLanguage
                )
            } else {
                _uiState.update {
                    it.copy(
                        loading = false,
                        translatedText = word.resText,
                        savedWord = true
                    )
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
                    translateText(
                        translated,
                        _uiState.value.originalLanguage,
                        _uiState.value.resLanguage
                    )
                } else {
                    _uiState.update {
                        it.copy(
                            loading = false,
                            translatedText = word.originalText,
                            savedWord = true
                        )
                    }
                }
            } else {
                val error = wordResult.exceptionOrNull()?.message
                if (error == NEW_WORD) {
                    translateText(
                        translated,
                        _uiState.value.originalLanguage,
                        _uiState.value.resLanguage
                    )
                } else {
                    Log.e("translateFromTranslated", "Error = $error")
                }
            }
        }
    }


    private fun changeLanguages() {
        _uiState.update {
            it.copy(
                resLanguage = it.originalLanguage,
                originalLanguage = it.resLanguage,
                inputText = "",
                translatedText = ""
            )
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
        private const val NEW_WORD = "Word does not exist"
    }
}
