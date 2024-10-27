package com.example.translatortrainer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.translatortrainer.test.ITestDataHelper
import com.example.translatortrainer.test.model.Level
import com.example.translatortrainer.test.model.Word
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CardSetViewModel(
    getCardSetUseCase: ITestDataHelper
) : ViewModel() {

    private var listOfCards: List<Word> = listOf()
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
        }
    }

    private fun addWordToKnow(word: Word) {
        val temp = listOfCards.toMutableList()
        val oldValueIndex = temp.indexOf(word)
        if (oldValueIndex != -1) {
            when (word) {
                is Word.WordUI -> temp[oldValueIndex] = word.copy(level = Level.KNOW)
                is Word.WordUIFromRes -> temp[oldValueIndex] = word.copy(level = Level.KNOW)
            }
            listOfCards = temp
            updateUI()
        }
    }

    private fun addWordToLearn(word: Word) {
        val temp = listOfCards.toMutableList()
        val oldValueIndex = temp.indexOf(word)
        if (oldValueIndex != -1) {
            when (word) {
                is Word.WordUI -> temp[oldValueIndex] = word.copy(level = Level.NEW)
                is Word.WordUIFromRes -> temp[oldValueIndex] = word.copy(level = Level.NEW)
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
        }
    }
}

data class CardSetState(
    val knowWords: Int = 10,
    val allWords: Int = 27,
    val words: Pair<Word, Word?>? = null,
)

sealed class CardSetIntent {
    data class AddWordToKnow(val word: Word.WordUI) : CardSetIntent()
    data class AddWordToLearn(val word: Word.WordUI) : CardSetIntent()
}
