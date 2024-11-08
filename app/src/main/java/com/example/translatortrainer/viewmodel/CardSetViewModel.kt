package com.example.translatortrainer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.WordStatus
import com.domain.usecase.IGetSetOfCardsUseCase
import com.domain.usecase.IGetWordByIdUseCase
import com.domain.usecase.IGetWordsOfSetUseCase
import com.domain.usecase.IUpdateWordUseCase
import com.example.translatortrainer.mapper.toWordUI
import com.example.translatortrainer.test.model.Level
import com.example.translatortrainer.test.model.WordUI
import com.example.translatortrainer.test.model.toWordEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardSetViewModel(
    setId: Int,
    private val getCardSet: IGetSetOfCardsUseCase,
    private val gerWords: IGetWordsOfSetUseCase,
    private val getWordById: IGetWordByIdUseCase,
    private val updateWord: IUpdateWordUseCase
) : ViewModel() {

    private var listOfCards: List<WordUI> = listOf()
    private val _uiState = MutableStateFlow(CardSetState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val setOfCards = getCardSet.invoke(setId)
            Log.e(TAG, "set = ${setOfCards?.name}, id = $setId")
            setOfCards?.id?.let {
                gerWords.invoke(it).collect { words ->
                    Log.e(TAG, "Words = ${words.size}, set = ${setOfCards.name}")
//                    if (listOfCards.isEmpty()) {
                    if (listOfCards.isEmpty()) {
                        listOfCards = words.map { it.toWordUI() }
                        _uiState.update {
                            it.copy(
                                name = setOfCards.name,
                                allWords = words.size,
                                knowWords = words.filter { it.status == WordStatus.LEARNED }.size,
                                words = Pair(listOfCards.first(), listOfCards.getOrNull(1))
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                knowWords = words.filter { it.status == WordStatus.LEARNED }.size,
                            )
                        }
                    }
                }
            }
        }
    }

    fun handleIntent(intent: CardSetIntent) {
        when (intent) {
            is CardSetIntent.AddWordToKnow -> addWordToKnow(word = intent.word)
            is CardSetIntent.AddWordToLearn -> addWordToLearn(word = intent.word)
            is CardSetIntent.ResetCardSet -> resetCardSet()
            is CardSetIntent.StartSelected -> {
                viewModelScope.launch {
                    val word = _uiState.value.words?.first?.id?.let { getWordById.invoke(it) }
                    word?.let {
                        updateWord.invoke(it.copy(status = WordStatus.LEARNED))
                    }
                }
            }
        }
    }

    private fun addWordToKnow(word: WordUI) {
        val temp = listOfCards.toMutableList()
        val oldValueIndex = temp.indexOf(word)
        if (oldValueIndex != -1) {
            temp[oldValueIndex] = word.copy(level = Level.KNOW)
            viewModelScope.launch {
                updateWord.invoke(temp[oldValueIndex].toWordEntity())
            }
            listOfCards = temp
            updateUI()
        }
    }

    private fun addWordToLearn(word: WordUI) {
        val temp = listOfCards.toMutableList()
        val oldValueIndex = temp.indexOf(word)
        if (oldValueIndex != -1) {
            temp[oldValueIndex] = word.copy(level = Level.NEW)
            viewModelScope.launch {
                updateWord.invoke(temp[oldValueIndex].toWordEntity())
            }
            listOfCards = temp
            updateUI()
        }
    }

    private fun updateUI() {
        val point = _uiState.value.words?.second
        if (point != null) {
            val index = listOfCards.indexOf(point)
            if (index != -1) {
                _uiState.update { state ->
                    state.copy(
                        knowWords = listOfCards.filter { it.level == Level.KNOW }.size,
                        words = Pair(point, listOfCards.getOrNull(index + 1))
                    )
                }
            }
        } else {
            _uiState.update { state ->
                state.copy(
                    knowWords = listOfCards.filter { it.level == Level.KNOW }.size,
                    words = null
                )
            }
        }
    }

    private fun resetCardSet() {
        val words = Pair(listOfCards.firstOrNull(), listOfCards.getOrNull(1))
        _uiState.update { state ->
            @Suppress("UNCHECKED_CAST")
            state.copy(
                words = if (words.first == null) null else (words as Pair<WordUI, WordUI?>)
            )
        }
    }

    companion object {
        private const val TAG = "CardSetViewModel"
    }
}

data class CardSetState(
    val knowWords: Int = 10,
    val allWords: Int = 27,
    val name: String = "Набор",
    val words: Pair<WordUI, WordUI?>? = null,
)

sealed class CardSetIntent {
    data class AddWordToKnow(val word: WordUI) : CardSetIntent()
    data class AddWordToLearn(val word: WordUI) : CardSetIntent()
    object ResetCardSet : CardSetIntent()
    object StartSelected : CardSetIntent()
}
