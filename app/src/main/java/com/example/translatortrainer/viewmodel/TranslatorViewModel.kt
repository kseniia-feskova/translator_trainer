package com.example.translatortrainer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.mock.model.SetOfCards
import com.data.mock.model.Status
import com.data.mock.model.Word
import com.data.model.WordEntity
import com.data.repository.translate.ITranslateRepository
import com.data.room.NEW_WORDS
import com.data.translate.Language
import com.domain.usecase.IAddWordUseCase
import com.domain.usecase.IGetSetOfAllCardsUseCase
import com.domain.usecase.IGetSetOfCardsUseCase
import com.domain.usecase.IGetWordsOfSetUseCase
import com.domain.usecase.toStatus
import com.example.translatortrainer.ui.screens.main.translate.model.TranslatorIntent
import com.example.translatortrainer.ui.screens.main.translate.model.TranslatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TranslatorViewModel(
    private val translateRepository: ITranslateRepository,
    private val addWordUseCase: IAddWordUseCase,
    private val getAllWordsUseCase: IGetSetOfAllCardsUseCase,
    private val getWordsFromSetUseCase: IGetWordsOfSetUseCase,
    private val getSetOfCards: IGetSetOfCardsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TranslatorState())
    val uiState = _uiState.asStateFlow()


    private val _setsOfCards = MutableStateFlow<List<SetOfCards>>(emptyList())
    val setsOfCards = _setsOfCards.asStateFlow()
    private var allSetID = ""


    init {
        viewModelScope.launch {
            val allSet = getAllWordsUseCase.invoke()
            val newWordsSet = getSetOfCards.invoke(NEW_WORDS)
            if (allSet != null) {
                getWordsFromSetUseCase.invoke(allSet.id).collect { wordList ->
                    allSetID = allSet.id.toString()
                    if (wordList.isNotEmpty()) {
                        _setsOfCards.update {
                            listOf(
                                SetOfCards(
                                    allSet.id,
                                    allSet.name,
                                    allSet.level,
                                    wordList.toWords()
                                )
                            )
                        }
                    }
                    Log.e(TAG, "All words = ${_setsOfCards.value}")
                }
                if (newWordsSet != null) {
                    Log.e(TAG, "New words set is not empty")
                    val newWords = getWordsFromSetUseCase.invoke(newWordsSet.id).last()
                    if (newWords.isNotEmpty()) {
                        _setsOfCards.update {
                            val new = _setsOfCards.value.toMutableSet()
                            new.add(
                                SetOfCards(
                                    newWordsSet.id,
                                    newWordsSet.name,
                                    newWordsSet.level,
                                    newWords.toWords()
                                )
                            )
                            new.toList()
                        }
                    }
                }
            } else {
                Log.e(TAG, "New all words = null")
            }
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
                        resLanguage = it.originalLanguage,
                        inputText = it.translatedText,
                        translatedText = it.inputText
                    )
                }
            }

            is TranslatorIntent.SaveWord -> {
                viewModelScope.launch {
                    val word = _uiState.value
                    Log.e(TAG, "Word = $word, setId = $allSetID")
                    addWordUseCase.invoke(
                        newWord = Word(
                            Word.createId(word.inputText, word.translatedText),
                            word.inputText,
                            word.translatedText,
                            status = Status.NEW
                        )
                    )
                }.invokeOnCompletion {

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
        return translateRepository.getTranslate(text, originalLanguage, resLanguage)
    }

    companion object {
        private const val TAG = "TranslatorViewModel"
    }
}

fun List<WordEntity>.toWords(): Set<Word> {
    return this.map {
        Word(
            id = it.id.toString(),
            text = it.original,
            translate = it.translation,
            status = it.status.toStatus()
        )
    }.toSet()
}
