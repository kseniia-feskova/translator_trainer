package com.translator.app.ui.screens.lesson.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.usecases.sets.IUpdateSetsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.model.Level
import presentation.model.WordUI
import com.translator.app.ui.screens.lesson.bubble.setId
import domain.usecases.words.IGetWordsBySetUseCase
import domain.usecases.words.IUpdateStatusUseCase
import mapper.toStatus
import mapper.toUI

abstract class BaseLessonViewModel(
    savedStateHandle: SavedStateHandle,
    private val getWordsBySetUseCase: IGetWordsBySetUseCase,
    private val updateStatusUseCase: IUpdateStatusUseCase,
    private val updateSetsUseCase: IUpdateSetsUseCase
) : ViewModel() {

    abstract val _baseState: MutableStateFlow<BaseLessonState>
    abstract fun checkIfLessonCompleted(): Boolean

    protected val setId = savedStateHandle.setId
    protected val words = mutableListOf<WordUI>()

    fun initializeWords(setWords: () -> Unit) {
        viewModelScope.launch {
            val response = getWordsBySetUseCase.invoke(setId)
            if (response.isSuccess) {
                val allWords =  response.getOrNull()?.map { it.toUI() }
                val newWords = allWords?.filter { it.level != Level.KNOW }
                if (newWords.isNullOrEmpty()) {
                    //TODO: replace count of words with variable, that depends from the difficulty level
                    words.addAll(allWords?.shuffled()?.take(5) ?: emptyList())
                } else {
                    words.addAll(newWords.take(5).toList())
                }
                setWords()
            }
        }
    }

    fun startTheTimer() {
        viewModelScope.launch {
            while (_baseState.value.time > 0L && !_baseState.value.isPaused && !_baseState.value.isLessonFailed) {
                delay(1000L)
                _baseState.update { it.copy(time = it.time - 1000L) }
            }

            if (_baseState.value.time <= 0L) {
                if (checkIfLessonCompleted()) {
                    _baseState.update { it.copy(isLessonCompleted = true) }
                } else {
                    _baseState.update { it.copy(isLessonFailed = true) }
                }
            }
        }
    }

    fun navigateToSuccess() {
        _baseState.update { it.copy(isLessonCompleted = false) }
    }

    open fun reload(time: Long = 1000L) {
        _baseState.update { BaseLessonState(time = time) }
        startTheTimer()
    }

    fun onPauseClicked() {
        _baseState.update { it.copy(isPaused = !it.isPaused) }
        if (!_baseState.value.isPaused) {
            startTheTimer()
        }
    }

    suspend fun updateWords() {
        words.forEach {
            updateStatusUseCase.invoke(it.id, level = it.level.inc().toStatus())
        }
        updateSetsUseCase.invoke()
    }

    suspend fun updateWords(words: List<WordUI>) {
        words.forEach {
            updateStatusUseCase.invoke(it.id, level = it.level.inc().toStatus())
        }
        updateSetsUseCase.invoke()
    }

}