package com.example.translatortrainer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.repository.translate.ITranslateRepository
import com.data.translate.Language
import com.example.translatortrainer.ui.screens.main.translate.model.TranslatorIntent
import com.example.translatortrainer.ui.screens.main.translate.model.TranslatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TranslatorViewModel(
    private val translateRepository: ITranslateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TranslatorState())
    val uiState = _uiState.asStateFlow()

    private fun translateText(
        inputText: String,
        originalLanguage: Language,
        resLanguage: Language
    ) {
        viewModelScope.launch {
            val translatedText = performTranslation(inputText, originalLanguage, resLanguage)
            _uiState.update { it.copy(translatedText = translatedText, showGlow = true) }
        }
    }


    fun handleIntent(intent: TranslatorIntent) {
        when (intent) {
            is TranslatorIntent.InputingText -> {
                onTextChange(intent.text)
            }

            is TranslatorIntent.EnterText -> {
                translateText(intent.text, intent.originalLanguage, intent.resLanguage)
            }

            is TranslatorIntent.HideGlow -> {
                _uiState.update { it.copy(showGlow = false) }
            }

            is TranslatorIntent.ChangeLanguages -> {
                _uiState.update {
                    it.copy(
                        originalLanguage = it.resLanguage,
                        resLanguage = it.originalLanguage
                    )
                }
            }
        }
    }

    private fun onTextChange(inputText: String) {
        if (inputText != _uiState.value.inputText) {
            _uiState.update { it.copy(inputText = inputText) }
        }
    }

    private suspend fun performTranslation(
        text: String,
        originalLanguage: Language,
        resLanguage: Language
    ): String {
        return translateRepository.getTranslate(text, originalLanguage, resLanguage)
    }

}
