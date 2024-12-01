package com.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.model.Level
import com.presentation.model.SetLevel
import com.presentation.model.SetOfCards
import com.presentation.model.WordUI
import com.presentation.ui.screens.main.translate.model.TranslatorIntent
import com.presentation.ui.screens.main.translate.model.TranslatorState
import com.presentation.usecases.IAddSetOfWordsUseCase
import com.presentation.usecases.IAddSetWordCrossRefUseCase
import com.presentation.usecases.IAddWordUseCase
import com.presentation.usecases.IGetFilteredWordsUseCase
import com.presentation.usecases.IGetSetOfAllCardsUseCase
import com.presentation.usecases.IGetSetOfCardsUseCase
import com.presentation.usecases.IGetWordsOfSetUseCase
import com.presentation.usecases.ITranslateWordUseCase
import com.presentation.utils.CURRENT_WORDS
import com.presentation.utils.Language
import com.presentation.utils.NEW_WORDS
import com.presentation.viewmodel.MainViewModel.Companion.allSetCards
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date


class TranslatorViewModel(
    private val translateWord: ITranslateWordUseCase,
    private val addWordUseCase: IAddWordUseCase,
    private val getAllWordsUseCase: IGetSetOfAllCardsUseCase,
    private val getWordsFromSetUseCase: IGetWordsOfSetUseCase,
    private val getSetOfCards: IGetSetOfCardsUseCase,
    private val getNewWords: IGetFilteredWordsUseCase,
    private val addSet: IAddSetOfWordsUseCase,
    private val addWordToSet: IAddSetWordCrossRefUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TranslatorState())
    val uiState = _uiState.asStateFlow()


    private val _setsOfAllCards = MutableStateFlow<SetOfCards?>(null)
    val setsOfAllCards = _setsOfAllCards.asStateFlow()

    private val _setsOfNewCards = MutableStateFlow<SetOfCards?>(null)
    val setsOfNewCards = _setsOfNewCards.asStateFlow()

    private val _currentSet = MutableStateFlow<SetOfCards?>(null)
    val currentSet = _currentSet.asStateFlow()

    private var allSetID = ""


    init {
        Log.d(TAG, "Init")
        viewModelScope.launch {
            val allSet = getAllWordsUseCase.invoke()
            val newWordsSet = getSetOfCards.invoke(NEW_WORDS)
            val currentSet = getSetOfCards.invoke(CURRENT_WORDS)
            if (allSet?.id != null) {
                val wordList = getWordsFromSetUseCase.invoke(allSet.id).first()
                allSetID = allSet.id.toString()
                if (wordList.isNotEmpty()) {
                    _setsOfAllCards.update {
                        SetOfCards(
                            allSet.id,
                            allSet.title,
                            allSet.level,
                            setOfWords = wordList.toSet(),
                            userId = allSet.userId
                        )
                    }
                }
                Log.e(TAG, "All words = ${_setsOfAllCards.value}")
            } else {
                addSet.invoke(allSetCards)
            }
            if (newWordsSet?.id != null) {
                launch {
                    getWordsFromSetUseCase.invoke(newWordsSet.id).collect { newWords ->
                        if (newWords.isNotEmpty()) {
                            _setsOfNewCards.update {
                                SetOfCards(
                                    newWordsSet.id,
                                    newWordsSet.title,
                                    newWordsSet.level,
                                    newWords.toSet(),
                                    userId = newWordsSet.userId
                                )
                            }
                        } else {
                            Log.e(TAG, "New all words = null")
                        }
                    }
                }
            } else {
                getNewWords.getWordsFilteredByDateOrStatus(
                    getPreviousDay(),
                    Date()
                ).collect { cards ->
                    val newWords = getSetOfCards.invoke(NEW_WORDS)
                    if (newWords == null) {
                        val newCardsSet = SetOfCards(
                            title = NEW_WORDS,
                            level = SetLevel.EASY,
                            userId = allSet?.userId ?: 0
                        )
                        val setId = addSet.invoke(newCardsSet)
                        if (setId != -1L) {
                            cards.forEach {
                                addWordToSet.invoke(wordID = it.id, setID = setId.toInt())
                            }
                        }
                    }
                }
            }
            if (currentSet?.id != null) {
                launch {
                    getWordsFromSetUseCase.invoke(currentSet.id).collect { newWords ->
                        if (newWords.isNotEmpty()) {
                            _currentSet.update {
                                SetOfCards(
                                    currentSet.id,
                                    currentSet.title,
                                    currentSet.level,
                                    newWords.toSet(),
                                    userId = currentSet.userId
                                )
                            }
                        } else {
                            Log.e(TAG, "New all words = null")
                        }
                    }
                }
            }
            Log.d(
                TAG,
                "All set = ${allSet?.id}, new words = ${newWordsSet?.id}, current words = ${currentSet?.id}"
            )
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

