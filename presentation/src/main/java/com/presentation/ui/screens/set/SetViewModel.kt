package com.presentation.ui.screens.set

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.model.Level
import com.presentation.model.WordUI
import com.presentation.usecases.IGetSetOfCardsUseCase
import com.presentation.usecases.IGetWordsOfSetUseCase
import com.presentation.usecases.IUpdateWordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetViewModel(
    savedStateHandle: SavedStateHandle,
    private val getWords: IGetWordsOfSetUseCase,
    private val getSet: IGetSetOfCardsUseCase,
    private val updateWord: IUpdateWordUseCase
) : ViewModel() {

    private val setId = savedStateHandle.setId

    private val _uiState = MutableStateFlow(SetUIState())
    val uiState = _uiState.asStateFlow()

    private val allWords: MutableList<WordUI> = mutableListOf()
    private var index = 0

    fun getSetId() = setId

    init {
        viewModelScope.launch {
            getWords.invoke(setId)
                .collect { setWithWords ->
                    allWords.clear()
                    allWords.addAll(setWithWords)
                    _uiState.update {
                        it.copy(
                            allWords = setWithWords.size,
                            knowWords = setWithWords.count { it.level == Level.KNOW }
                        )
                    }
                    updateUI()
                }
        }
        viewModelScope.launch {
            _uiState.update { it.copy(name = getSet.invoke(setId)?.title.toString()) }
        }
    }

    fun handleIntent(intent: CardSetIntent) {
        when (intent) {
            is CardSetIntent.AddWordToKnow -> addWordToKnow(intent.word)
            is CardSetIntent.AddWordToLearn -> addWordToLearn(intent.word)
            is CardSetIntent.ResetCardSet -> resetCardSet()
        }
    }

    private fun resetCardSet() {
        index = 0
        updateUI()
    }

    private fun addWordToKnow(word: WordUI) {
        viewModelScope.launch {
            updateWord.invoke(word.copy(level = Level.KNOW))
            index++
            updateUI()
        }
    }


    private fun addWordToLearn(word: WordUI) {
        viewModelScope.launch {
            updateWord.invoke(word.copy(level = Level.NEW))
            index++
            updateUI()
        }
    }


    private fun updateUI() {
        val first = allWords.getOrNull(index)
        val second = allWords.getOrNull(index + 1)
        _uiState.update {
            it.copy(words = if (first != null) Pair(first, second) else null)
        }
    }
}

