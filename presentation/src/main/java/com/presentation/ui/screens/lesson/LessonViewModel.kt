package com.presentation.ui.screens.lesson

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.model.Level
import com.presentation.model.WordUI
import com.presentation.ui.screens.set.setId
import com.presentation.usecases.words.IGetWordsOfSetUseCase
import com.presentation.usecases.words.IUpdateWordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LessonViewModel(
    savedStateHandle: SavedStateHandle,
    private val getSetOfWords: IGetWordsOfSetUseCase,
    private val updateWord: IUpdateWordUseCase
) : ViewModel() {

    private val setId = savedStateHandle.setId
    private val type = savedStateHandle.type

    private val _uiState = MutableStateFlow(type.mapToUI().copy(preLoading = true))
    val uiState = _uiState.asStateFlow()

    private val allWords = mutableListOf<WordUI>()

    init {
        viewModelScope.launch {
            getSetOfWords.invoke(setId).collectLatest {
                if (allWords.isEmpty()) {
                    val filtered = it.filter { it.level != Level.KNOW }
                    val selected = filtered.take(if (filtered.size < 5) filtered.size else 5)
                    allWords.clear()
                    allWords.addAll(selected.shuffled())
                    initLesson(allWords)
                }
            }
        }
    }

    fun handleIntent(intent: LessonIntent) {
        when (intent) {
            is LessonIntent.DontKnow -> handleDontKnow()
            is LessonIntent.OptionSelected -> checkSelectedOption(intent.selected)
            is LessonIntent.Start -> handleStartLesson()
        }
    }

    private fun handleDontKnow() {
        Log.e("LessonViewModel", "handleDontKnow")
        when (type) {
            LessonType.TRANSLATE -> {
                val current = (_uiState.value.lessonData as Lesson.TranslateLesson?)?.correctWord
                if (current != null) {
                    allWords.remove(current)
                    allWords.add(current)
                    updateCard()
                }
            }

            LessonType.ORIGIN -> {
                val current = (_uiState.value.lessonData as Lesson.OriginLesson?)?.correctWord
                if (current != null) {
                    allWords.remove(current)
                    allWords.add(current)
                    updateCard()
                }
            }
        }
    }

    private fun checkSelectedOption(option: String?) {
        Log.e("LessonViewModel", "checkSelectedOption = $option")
        val current = _uiState.value.lessonData?.correctOption
        if (current == option) {
            Log.e("LessonViewModel", "checkSelectedOption = correct")
            _uiState.update {
                it.copy(index = it.index + 1)
            }
            updateCard()
        } else {
            Log.e("LessonViewModel", "checkSelectedOption = incorrect")
        }
    }

    private fun updateCard() {
        if (_uiState.value.index < allWords.size) {
            when (type) {
                LessonType.TRANSLATE -> {
                    _uiState.update {
                        it.copy(
                            lessonData = (it.lessonData as Lesson.TranslateLesson).copy(
                                correctWord = allWords[_uiState.value.index],
                                selectedWord = null,
                                words = it.lessonData.words.shuffled().toSet()
                            ),
                        )
                    }
                }

                LessonType.ORIGIN -> {
                    _uiState.update {
                        it.copy(
                            lessonData = (it.lessonData as Lesson.OriginLesson).copy(
                                correctWord = allWords[_uiState.value.index],
                                selectedWord = null,
                                words = it.lessonData.words.shuffled().toSet()
                            )
                        )
                    }
                }
            }
        } else {
            handleFinishLesson()
            Log.e("LessonViewModel", "Lesson complete")
        }
    }

    private fun handleStartLesson() {
        Log.e("LessonViewModel", "handleStartLesson")
        _uiState.update {
            it.copy(preLoading = false)
        }

    }

    private fun initLesson(words: List<WordUI>) {
        val lessonData = when (type) {
            LessonType.TRANSLATE -> {
                Lesson.TranslateLesson(
                    words = words.shuffled().toSet(),
                    correctWord = words[_uiState.value.index],
                    selectedWord = null,
                )
            }

            LessonType.ORIGIN -> {
                Lesson.OriginLesson(
                    words = words.shuffled().toSet(),
                    correctWord = words[_uiState.value.index],
                    selectedWord = null
                )
            }
        }
        if (lessonData.options.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    showStart = true,
                    lessonData = lessonData
                )
            }
        }
    }

    private fun handleFinishLesson() {
        viewModelScope.launch {
            allWords.map {
                updateWord(it)
            }
            _uiState.update {
                it.copy(
                    complete = true
                )
            }
        }
    }

    private suspend fun updateWord(word: WordUI) {
        updateWord.invoke(word.copy(level = word.level.inc()))
    }
}
