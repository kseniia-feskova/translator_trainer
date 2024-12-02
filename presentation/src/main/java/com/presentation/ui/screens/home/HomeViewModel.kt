package com.presentation.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.model.Level
import com.presentation.model.WordUI
import com.presentation.usecases.IAddWordUseCase
import com.presentation.usecases.ITranslateWordUseCase
import com.presentation.utils.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date


class HomeViewModel(
    private val translateWord: ITranslateWordUseCase,
    private val addWordUseCase: IAddWordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState = _uiState.asStateFlow()

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.InputText -> {
                onTextChange(intent.text)
            }

            is HomeIntent.EnterText -> {
                //поискать слово в словаре пользователя
                //если есть - то иконка цветная, сохранить нельзя
                //если нет - то топаем в запрос, сохранить можно
                translateText(
                    intent.text,
                    _uiState.value.originalLanguage,
                    _uiState.value.resLanguage
                )
            }

            is HomeIntent.SaveWord -> {
                viewModelScope.launch {
                    val word = _uiState.value
                    addWordUseCase.invoke(
                        newWord = WordUI(
                            id = 0,
                            originalText = word.inputText,
                            resText = word.translatedText,
                            level = Level.NEW,
                            date = Date()
                        )
                    )
                }
            }
        }
    }

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

    private fun onTextChange(inputText: String) {
        if (inputText != _uiState.value.inputText) {
            _uiState.update {
                it.copy(
                    inputText = inputText,
                    translatedText = if (it.translatedText.isNotEmpty()) "" else it.translatedText
                )
            }
        }
    }

    private suspend fun performTranslation(
        text: String,
        originalLanguage: Language,
        resLanguage: Language
    ): String {
        return translateWord.invoke(text, originalLanguage, resLanguage)
    }

    companion object {
        private const val TAG = "TranslatorViewModel"
    }
}

