package presentation.ui.screens.lesson.bubble

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import domain.usecases.sets.IUpdateSetsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.model.LessonType
import presentation.ui.screens.lesson.base.BaseLessonState
import presentation.ui.screens.lesson.base.BaseLessonViewModel
import domain.usecases.words.IGetWordsBySetUseCase
import domain.usecases.words.IUpdateStatusUseCase


class BubbleLessonViewModel(
    savedStateHandle: SavedStateHandle,
    getWordsBySetUseCase: IGetWordsBySetUseCase,
    updateStatusUseCase: IUpdateStatusUseCase,
    updateSetsUseCase: IUpdateSetsUseCase
) : BaseLessonViewModel(
    savedStateHandle,
    getWordsBySetUseCase,
    updateStatusUseCase,
    updateSetsUseCase
) {

    private val _bubbles = MutableStateFlow<List<BubbleWitText>>(emptyList())
    val bubbles: StateFlow<List<BubbleWitText>> = _bubbles.asStateFlow()

    override val _baseState = MutableStateFlow(BaseLessonState(time = TIMER))
    val baseState = _baseState.asStateFlow()

    private val selectedBubbles = mutableListOf<BubbleWitText>()

    override fun checkIfLessonCompleted(): Boolean = _bubbles.value.find { it.isVisible } == null

    init {
        startTheTimer()
    }

    override fun reload(time: Long) {
        super.reload(TIMER)
        _bubbles.update {
            it.map {
                it.copy(isVisible = true, isWrong = null, isSelected = false)
            }.shuffled()
        }
    }

    fun initializeBubbles(screenWidth: Float, screenHeight: Float) {
        viewModelScope.launch {
            initializeWords {
                val selected = words.map { it.toBubbles() }.flatten()
                val list = selected.map {
                    getBubble(
                        screenWidthPx = screenWidth,
                        screenHeightPx = screenHeight,
                        sizeKoef = (it.word.length + 4) * 16f,
                        color = it.bgColor,
                        word = it.word,
                        wordId = it.wordId,
                        textColor = it.textColor
                    )
                }.shuffled()
                _bubbles.update { list }
            }
        }
    }

    fun navigateToSuccess(navigate: (LessonType, Int) -> Unit) {
        super.navigateToSuccess()
        navigate(LessonType.BUBBLE, _bubbles.value.size / 2)
    }

    fun onBubbleClick(bubbleId: String) {
        val currentBubbles = _bubbles.value.toMutableList()
        val bubble = currentBubbles.firstOrNull { it.id == bubbleId }
        if (bubble == null) return

        if (selectedBubbles.find { it.id == bubbleId } != null) {
            selectedBubbles.removeIf { it.id == bubbleId }
            updateBubbleState(currentBubbles, bubble.copy(isSelected = false), null)
        } else if (selectedBubbles.size < 2) {
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
                _baseState.update {
                    it.copy(
                        lives = it.lives.dec(),
                        isLessonFailed = it.lives.dec() == 0
                    )
                }
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
            _baseState.update {
                it.copy(
                    isLessonCompleted = _bubbles.value.find { it.isVisible } == null
                )
            }
        }
    }

    companion object {
        private const val TIMER = 30000L
    }
}
