package com.presentation.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.model.Level
import com.presentation.model.SetLevel
import com.presentation.model.SetOfCards
import com.presentation.model.WordUI
import com.presentation.usecases.IAddSetOfWordsUseCase
import com.presentation.usecases.IAddWordUseCase
import com.presentation.usecases.IFindWordByOriginUseCase
import com.presentation.usecases.IFindWordByTranslatedUseCase
import com.presentation.usecases.IGetSetOfAllCardsUseCase
import com.presentation.usecases.ITranslateWordUseCase
import com.presentation.utils.ALL_WORDS
import com.presentation.utils.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date


class HomeViewModel(
    private val findWordByOrigin: IFindWordByOriginUseCase,
    private val findWordByTranslate: IFindWordByTranslatedUseCase,
    private val translateWord: ITranslateWordUseCase,
    private val getAllCards: IGetSetOfAllCardsUseCase,
    private val addSetOfCards: IAddSetOfWordsUseCase,
    private val addWordUseCase: IAddWordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState = _uiState.asStateFlow()
    private var setId: Int? = null

    init {
        viewModelScope.launch {
            val allCards = getAllCards.invoke()
            if (allCards == null) {
                addSetOfCards.invoke(
                    SetOfCards(0, ALL_WORDS, SetLevel.EASY, emptySet(), 0)
                )
                setId = getAllCards.invoke()?.id
            }else {
                setId = allCards.id
            }
        }
    }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.InputText -> {
                onTextChange(intent.text)
            }

            is HomeIntent.EnterText -> {
                _uiState.update { it.copy(loading = true) }
                if (_uiState.value.originalLanguage == Language.RUSSIAN) {
                    translateFromOrigin(intent.text)
                } else {
                    translateFromTranslated(intent.text)
                }
            }

            is HomeIntent.SaveWord -> {
                viewModelScope.launch {
                    val word = _uiState.value
                    Log.e("handleIntent.Save", "Save word")
                    if (word.originalLanguage == Language.RUSSIAN) {
                        addWordUseCase.invoke(
                            setId = setId,
                            newWord = WordUI(
                                id = 0,
                                originalText = word.inputText,
                                resText = word.translatedText,
                                level = Level.NEW,
                                date = Date()
                            )
                        )
                    } else {
                        addWordUseCase.invoke(
                            setId = setId,
                            newWord = WordUI(
                                id = 0,
                                originalText = word.translatedText,
                                resText = word.inputText,
                                level = Level.NEW,
                                date = Date()
                            )
                        )
                    }
                    _uiState.update { it.copy(savedWord = true) }
                }
            }

            is HomeIntent.ChangeLanguages -> {
                changeLanguages()
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

    private suspend fun performTranslation(
        text: String,
        originalLanguage: Language,
        resLanguage: Language
    ): String {
        return translateWord.invoke(text, originalLanguage, resLanguage)
    }

    private fun translateFromOrigin(origin: String) {
        viewModelScope.launch {
            val word = findWordByOrigin.invoke(origin)
            Log.e(
                "handleIntent.Enter",
                "Find word by origin = ${origin}, word = $word"
            )
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
            val word = findWordByTranslate.invoke(translated)
            Log.e(
                "handleIntent.Enter",
                "Find word by translate = ${translated}, word = $word"
            )
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
    }
}

