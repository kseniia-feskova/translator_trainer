package com.presentation.ui.screens.set

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import presentation.model.Level
import presentation.model.WordUI
import presentation.ui.screens.auth.BaseError
import presentation.usecases.words.IGetWordsBySetUseCase
import presentation.usecases.words.IUpdateStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.ui.screens.set.CardSetIntent
import presentation.ui.screens.set.SetUIState
import presentation.ui.screens.set.setId
import presentation.ui.screens.set.setName

class SetViewModel(
    savedStateHandle: SavedStateHandle,
    private val getWords: IGetWordsBySetUseCase,
    private val updateWord: IUpdateStatusUseCase
) : ViewModel() {

    private val setId = savedStateHandle.setId
    private val setName: String = savedStateHandle.setName

    private val _uiState = MutableStateFlow(SetUIState())
    val uiState = _uiState.asStateFlow()

    private val allWords: MutableList<WordUI> = mutableListOf()
    private val knownWords: MutableList<WordUI> = mutableListOf()
    private var index = 0

    fun getSetId() = setId

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val response = getWords.invoke(setId)
            if (response.isSuccess) {
                val words = response.getOrNull() ?: emptyList()
                allWords.addAll(words)
                knownWords.addAll(words.filter { it.level == Level.KNOW })
                _uiState.update {
                    it.copy(
                        name = setName,
                        allWords = allWords.size
                    )
                }
                updateUI()
            } else {
                handleError(response)
            }
        }
    }

    fun handleIntent(intent: CardSetIntent) {
        when (intent) {
            is CardSetIntent.AddWordToKnow -> addWordToKnow(intent.word)
            is CardSetIntent.AddWordToLearn -> addWordToLearn(intent.word)
            is CardSetIntent.ResetCardSet -> resetCardSet()
            is CardSetIntent.CourseSelection -> _uiState.update { it.copy(selectLessonVisible = intent.isVisible) }
        }
    }

    private fun resetCardSet() {
        index = 0
        allWords.clear()
        knownWords.clear()
        loadData()
    }

    private fun addWordToKnow(word: WordUI) {
        viewModelScope.launch {
            val result = updateWord.invoke(word.id, level = Level.KNOW)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    if (!knownWords.contains(it)) {
                        knownWords.add(it)
                    }
                    index++
                    updateUI()
                }
            } else {
                handleError(result)
            }
        }
    }


    private fun addWordToLearn(word: WordUI) {
        viewModelScope.launch {
            val result = updateWord.invoke(word.id, level = Level.NEW)
            if (result.isSuccess) {
                result.getOrNull()?.let { word ->
                    knownWords.removeIf { it.id == word.id }
                    index++
                    updateUI()
                }
            } else {
                handleError(result)
            }
        }
    }


    private fun updateUI() {
        val first = allWords.getOrNull(index)
        val second = allWords.getOrNull(index + 1)
        _uiState.update {
            it.copy(
                words = if (first != null) Pair(first, second) else null,
                knowWords = knownWords.size
            )
        }
    }

    private fun <T> handleError(response: Result<T>) {
        val error = response.exceptionOrNull()
        val errorMsg = when (error?.message) {
            "Failed to connect" -> BaseError.INTERNET_CONNECTION_ERROR
            else -> BaseError.DEFAULT
        }
        _uiState.update { it.copy(error = errorMsg, loading = false) }
    }
}

