package com.presentation.ui.screens.lesson.bubble

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.model.LessonType
import com.presentation.model.Level
import com.presentation.model.WordUI
import com.presentation.usecases.sets.IUpdateSetsUseCase
import com.presentation.usecases.words.IGetWordsBySetUseCase
import com.presentation.usecases.words.IUpdateStatusUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class BubbleLessonViewModel(
    savedStateHandle: SavedStateHandle,
    private val getWordsBySetUseCase: IGetWordsBySetUseCase,
    private val updateStatusUseCase: IUpdateStatusUseCase,
    private val updateSetsUseCase: IUpdateSetsUseCase
) : ViewModel() {
    private val _bubbles = MutableStateFlow<List<BubbleWitText>>(emptyList())
    val bubbles: StateFlow<List<BubbleWitText>> = _bubbles.asStateFlow()

    private val _lessonComplete = MutableStateFlow(false)
    val lessonComplete = _lessonComplete.asStateFlow()

    private val _lessonFailed = MutableStateFlow(false)
    val lessonFailed = _lessonFailed.asStateFlow()

    private val _onPause = MutableStateFlow(false)
    val onPause = _onPause.asStateFlow()

    private val setId = savedStateHandle.setId

    private val selectedBubbles = mutableListOf<BubbleWitText>()
    private val words = mutableListOf<WordUI>()

    private val _lives = MutableStateFlow(3)
    val lives = _lives.asStateFlow()

    fun onPauseClicked() {
        _onPause.update { !it }
    }

    fun initializeBubbles(screenWidth: Float, screenHeight: Float) {
        viewModelScope.launch {
            val response = getWordsBySetUseCase.invoke(setId)
            if (response.isSuccess) {
                val words = response.getOrNull()?.filter { it.level != Level.KNOW }
                val selected = words?.map {
                    it.toBubbles()
                }?.flatten()
                if (words != null) {
                    this@BubbleLessonViewModel.words.addAll(words)
                }
                Log.e("BubbleLessonVM", "selected = $selected")

                val list = selected?.map {
                    getBubble(
                        screenWidthPx = screenWidth,
                        screenHeightPx = screenHeight,
                        sizeKoef = (it.word.length + 5) * 20f,
                        color = it.bgColor,
                        word = it.word,
                        wordId = it.wordId,
                        textColor = it.textColor
                    )
                }?.shuffled() ?: emptyList()
                Log.e("BubbleLessonVM", "list = $list")
                _bubbles.update { list }
            } else {
                //handleError(words)
            }
        }
    }

    fun navigateToSuccess(navigate: (LessonType, Int) -> Unit) {
        _lessonComplete.update { false }
        navigate(LessonType.BUBBLE, _bubbles.value.size / 2)
    }

    fun reload() {
        _lives.update { 3 }
        _lessonFailed.update { false }
        _bubbles.update { it.map { it.copy(isVisible = true, isWrong = null, isSelected = false) } }
    }

    fun onBubbleClick(bubbleId: String) {
        val currentBubbles = _bubbles.value.toMutableList()
        val bubble = currentBubbles.firstOrNull { it.id == bubbleId }
        if (bubble == null) return

        if (selectedBubbles.find { it.id == bubbleId } != null) {
            // 🔵 Убираем подсветку и возобновляем движение
            selectedBubbles.removeIf { it.id == bubbleId }
            updateBubbleState(currentBubbles, bubble.copy(isSelected = false), null)
        } else if (selectedBubbles.size < 2) {
            // 📌 Добавляем пузырь в выбор
            selectedBubbles.add(bubble.copy(isSelected = true))
            updateBubbleState(currentBubbles, bubble.copy(isSelected = true), null)

            if (selectedBubbles.size == 2) {
                checkSelectedBubbles(currentBubbles)
            }
        }
    }

    private fun checkSelectedBubbles(currentBubbles: MutableList<BubbleWitText>) {
        val (first, second) = selectedBubbles

        if (first.wordId == second.wordId) {
            viewModelScope.launch {
                updateBubbleState(
                    currentBubbles,
                    first.copy(isWrong = false),
                    second.copy(isWrong = false)
                )
                delay(500) // 2 секунды подсветки
                updateBubbleState(
                    currentBubbles,
                    first.copy(isVisible = false, isSelected = false),
                    second.copy(isVisible = false, isSelected = false)
                )
                selectedBubbles.clear()
            }
        } else {
            // ❌ Разные: временно красная подсветка
            viewModelScope.launch {
                updateBubbleState(
                    currentBubbles,
                    first.copy(isWrong = true),
                    second.copy(isWrong = true)
                )
                delay(500) // 2 секунды подсветки
                updateBubbleState(
                    currentBubbles,
                    first.copy(isWrong = null, isSelected = false),
                    second.copy(isWrong = null, isSelected = false)
                )
                _lives.update { it - 1 }
                checkFail()
                selectedBubbles.clear()
            }
        }
    }

    private fun updateBubbleState(
        bubbles: MutableList<BubbleWitText>,
        newBubble: BubbleWitText,
        secondNewBubble: BubbleWitText?
    ) {
        val index = bubbles.indexOfFirst { it.id == newBubble.id }
        val secondIndex = bubbles.indexOfFirst { it.id == secondNewBubble?.id }
        if (secondNewBubble == null) {
            if (index != -1) {
                bubbles[index] = newBubble
                _bubbles.update { bubbles.toList() }

            }
        } else {
            if (index != -1 && secondIndex != -1) {
                bubbles[index] = newBubble
                bubbles[secondIndex] = secondNewBubble
                _bubbles.update { bubbles.toList() }
            }
        }
        checkEmpty()
    }

    private fun checkEmpty() {
        viewModelScope.launch {
            updateWords()
            _lessonComplete.update { _bubbles.value.find { it.isVisible } == null }
        }
    }

    private suspend fun updateWords() {
        words.forEach {
            updateStatusUseCase.invoke(it.id, level = it.level.inc())
        }
        updateSetsUseCase.invoke()
    }

    private fun checkFail() {
        _lessonFailed.update { _lives.value == 0 }
    }
}
