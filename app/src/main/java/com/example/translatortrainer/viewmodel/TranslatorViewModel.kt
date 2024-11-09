package com.example.translatortrainer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.mock.model.SetOfCards
import com.data.mock.model.Status
import com.data.mock.model.Word
import com.data.model.SetLevel
import com.data.model.SetOfWords
import com.data.model.WordEntity
import com.data.repository.translate.ITranslateRepository
import com.data.room.CURRENT_WORDS
import com.data.room.NEW_WORDS
import com.data.translate.Language
import com.domain.usecase.IAddSetOfWordsUseCase
import com.domain.usecase.IAddSetWordCrossRef
import com.domain.usecase.IAddWordUseCase
import com.domain.usecase.IGetFilteredWordsUseCase
import com.domain.usecase.IGetSetOfAllCardsUseCase
import com.domain.usecase.IGetSetOfCardsUseCase
import com.domain.usecase.IGetWordsOfSetUseCase
import com.domain.usecase.toStatus
import com.example.translatortrainer.ui.screens.main.translate.model.TranslatorIntent
import com.example.translatortrainer.ui.screens.main.translate.model.TranslatorState
import com.example.translatortrainer.viewmodel.MainViewModel.Companion.allSetCards
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date


class TranslatorViewModel(
    private val translateRepository: ITranslateRepository,
    private val addWordUseCase: IAddWordUseCase,
    private val getAllWordsUseCase: IGetSetOfAllCardsUseCase,
    private val getWordsFromSetUseCase: IGetWordsOfSetUseCase,
    private val getSetOfCards: IGetSetOfCardsUseCase,
    private val getNewWords: IGetFilteredWordsUseCase,
    private val addSet: IAddSetOfWordsUseCase,
    private val addWordToSet: IAddSetWordCrossRef
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
        viewModelScope.launch {
            val allSet = getAllWordsUseCase.invoke()
            val newWordsSet = getSetOfCards.invoke(NEW_WORDS)
            val currentSet = getSetOfCards.invoke(CURRENT_WORDS)
            if (allSet != null) {
                launch {
                    getWordsFromSetUseCase.invoke(allSet.id).collect { wordList ->
                        allSetID = allSet.id.toString()
                        if (wordList.isNotEmpty()) {
                            _setsOfAllCards.update {
                                SetOfCards(
                                    allSet.id,
                                    allSet.name,
                                    allSet.level,
                                    wordList.toWords()
                                )
                            }
                        }
                        Log.e(TAG, "All words = ${_setsOfAllCards.value}")
                    }
                }
            } else {
                addSet.invoke(allSetCards)
            }
            Log.e(TAG, "NewWords $newWordsSet")
            if (newWordsSet != null) {
                Log.e(TAG, "New words set is not empty")
                launch {
                    getWordsFromSetUseCase.invoke(newWordsSet.id).collect { newWords ->
                        if (newWords.isNotEmpty()) {
                            _setsOfNewCards.update {
                                SetOfCards(
                                    newWordsSet.id,
                                    newWordsSet.name,
                                    newWordsSet.level,
                                    newWords.toWords()
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
                        Log.e(TAG, "New words = $cards")
                        val newCradsSet = SetOfWords(
                            name = NEW_WORDS,
                            level = SetLevel.EASY,
                            userId = allSet?.userId ?: 0
                        )
                        val setId = addSet.invoke(newCradsSet)
                        if (setId != -1L) {
                            Log.e("NEW CARDS", "Response = $setId")
                            cards.forEach {
                                addWordToSet.invoke(wordID = it.id, setID = setId.toInt())
                            }
                        }
                    }
                }
            }
            if (currentSet != null) {
                Log.e(TAG, "currentSet is not empty")
                launch {
                    getWordsFromSetUseCase.invoke(currentSet.id).collect { newWords ->
                        if (newWords.isNotEmpty()) {
                            _currentSet.update {
                                SetOfCards(
                                    currentSet.id,
                                    currentSet.name,
                                    currentSet.level,
                                    newWords.toWords()
                                )
                            }
                        } else {
                            Log.e(TAG, "New all words = null")
                        }
                    }
                }
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
