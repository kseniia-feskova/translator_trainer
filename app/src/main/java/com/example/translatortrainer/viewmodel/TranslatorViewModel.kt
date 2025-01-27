package com.example.translatortrainer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.repository.translate.ITranslateRepository
import com.example.translatortrainer.ui.screens.main.translate.model.TranslatorIntent
import com.example.translatortrainer.ui.screens.main.translate.model.TranslatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


class TranslatorViewModel(
    private val translateRepository: ITranslateRepository
) : ViewModel() {
    private val _inputText = MutableStateFlow("")
    private val _uiState = MutableStateFlow(TranslatorState())
    val uiState = _uiState.asStateFlow()

    init {
        //TODO - refactor!!!
        viewModelScope.launch {
            _inputText
                .debounce(3000) // Дебаунс с задержкой 3 секунды
                .filter { it.isNotEmpty() } // Игнорируем пустые строки
                .collect { text ->
                    val translated = performTranslation(text)
                    _uiState.value = _uiState.value.copy(translatedText = translated)
                }
        }
    }

    fun handleIntent(intent: TranslatorIntent) {
        when (intent) {
            is TranslatorIntent.EnterText -> {
                _inputText.value = intent.text
                _uiState.value = _uiState.value.copy(inputText = intent.text)
            }
        }
    }


    private suspend fun performTranslation(text: String): String {
        return translateRepository.getTranslate(text) ?: ""
    }

}
