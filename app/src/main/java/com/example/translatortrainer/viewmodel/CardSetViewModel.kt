package com.example.translatortrainer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.translatortrainer.test.ITestDataHelper
import com.example.translatortrainer.test.model.Level
import com.example.translatortrainer.test.model.WordUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CardSetViewModel(
    private val getCardSetUseCase: ITestDataHelper
) : ViewModel() {

    private var listOfCards: List<WordUI> = listOf()
    private val _uiState = MutableStateFlow(CardSetState())
    val uiState = _uiState.asStateFlow()

    init {
        listOfCards = getCardSetUseCase.getListOfWords()
        _uiState.update {
            it.copy(
                allWords = listOfCards.size,
                knowWords = listOfCards.filter { it.level == Level.KNOW }.size,
                words = Pair(listOfCards.first(), listOfCards.getOrNull(1))
            )
        }
    }

    fun handleIntent(intent: CardSetIntent) {
        when (intent) {
            is CardSetIntent.AddWordToKnow -> addWordToKnow(word = intent.word)
            is CardSetIntent.AddWordToLearn -> addWordToLearn(word = intent.word)
            is CardSetIntent.ResetCardSet -> resetCardSet()
            is CardSetIntent.SaveSelected -> getCardSetUseCase.updateList(
                listOfCards.filter { it.level != Level.KNOW }
            )
        }
    }

    private fun addWordToKnow(word: WordUI) {
        val temp = listOfCards.toMutableList()
        val oldValueIndex = temp.indexOf(word)
        if (oldValueIndex != -1) {
            temp[oldValueIndex] = word.copy(level = Level.KNOW)
            listOfCards = temp
            updateUI()
        }
    }

    private fun addWordToLearn(word: WordUI) {
        val temp = listOfCards.toMutableList()
        val oldValueIndex = temp.indexOf(word)
        if (oldValueIndex != -1) {
            temp[oldValueIndex] = word.copy(level = Level.NEW)
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
}

data class CardSetState(
    val knowWords: Int = 10,
    val allWords: Int = 27,
    val words: Pair<WordUI, WordUI?>? = null,
)

sealed class CardSetIntent {
    data class AddWordToKnow(val word: WordUI) : CardSetIntent()
    data class AddWordToLearn(val word: WordUI) : CardSetIntent()
    object ResetCardSet : CardSetIntent()
    object SaveSelected : CardSetIntent()
}
