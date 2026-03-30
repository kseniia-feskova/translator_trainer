package presentation.ui.screens.lesson.match

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import domain.usecases.sets.IUpdateSetsUseCase
import domain.usecases.words.IGetWordsBySetUseCase
import domain.usecases.words.IUpdateStatusUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.model.LessonType
import presentation.ui.screens.lesson.base.BaseLessonState
import presentation.ui.screens.lesson.base.BaseLessonViewModel
import presentation.ui.screens.lesson.match.model.MatchViewData
import java.util.UUID

class MatchLessonViewModel(
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
    private val _state = MutableStateFlow<MatchLessonState?>(null)
    val state = _state.asStateFlow()

    override val _baseState = MutableStateFlow(BaseLessonState(time = TIMER))
    val baseState = _baseState.asStateFlow()

    override fun checkIfLessonCompleted(): Boolean =
        _state.value?.leftColumn?.none { it is MatchViewData.MatchWordData } == true

    override fun reload(time: Long) {
        super.reload(TIMER)
        setWordsPairs()
    }

    init {
        startTheTimer()
        initializeWords {
            setWordsPairs()
        }
    }

    fun navigateToSuccess(navigate: (LessonType, Int) -> Unit) {
        super.navigateToSuccess()
        navigate(LessonType.MATCH, words.size)
    }

    fun onWordSelected(word: MatchViewData) {
        when (word) {
            is MatchViewData.MatchWordData -> {
                val selectedLeft =
                    _state.value?.leftColumn?.firstOrNull { it is MatchViewData.MatchWordData && it.isSelected }
                val selectedRight =
                    _state.value?.rightColumn?.firstOrNull { it is MatchViewData.MatchWordData && it.isSelected }
                val isWordOnLeft =
                    _state.value?.leftColumn?.firstOrNull { it.id == word.id } != null

                if (isWordOnLeft) {
                    if (selectedLeft == null) {
                        selectLeftWord(word)
                        if (selectedRight != null && selectedRight is MatchViewData.MatchWordData) {
                            viewModelScope.launch {
                                delay(200L)
                                compareSelectedWords(word, selectedRight)
                            }
                        }
                    }
                } else {
                    if (selectedRight == null) {
                        selectRightWord(word)
                        if (selectedLeft != null && selectedLeft is MatchViewData.MatchWordData) {
                            viewModelScope.launch {
                                delay(200L)
                                compareSelectedWords(selectedLeft, word)
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }

    private fun setWordsPairs() {
        val leftColumn = words.map {
            MatchViewData.MatchWordData(
                id = UUID.randomUUID().toString(),
                text = it.resText
            )
        }.shuffled()

        val rightColumn = words.map {
            MatchViewData.MatchWordData(
                id = UUID.randomUUID().toString(),
                text = it.originalText
            )
        }.shuffled()

        _state.update {
            MatchLessonState(
                leftColumn = leftColumn,
                rightColumn = rightColumn
            )
        }
    }

    private fun selectLeftWord(word: MatchViewData.MatchWordData) {
        val newLeftColumn = _state.value?.leftColumn?.map {
            if (it.id == word.id) {
                word.copy(isSelected = true)
            } else it
        } ?: emptyList()
        val newState = _state.value?.copy(leftColumn = newLeftColumn.toList())
        _state.update { newState }
    }

    private fun selectRightWord(word: MatchViewData.MatchWordData) {
        val newRightColumn = _state.value?.rightColumn?.map {
            if (it.id == word.id) {
                word.copy(isSelected = true)
            } else it
        } ?: emptyList()
        val newState = _state.value?.copy(rightColumn = newRightColumn.toList())
        _state.update { newState }
    }


    private fun compareSelectedWords(
        leftWord: MatchViewData.MatchWordData,
        rightWord: MatchViewData.MatchWordData
    ) {
        val selectedWordUI = words.firstOrNull { it.resText == leftWord.text }
        if (selectedWordUI != null) {
            if (selectedWordUI.originalText == rightWord.text) {
                removeMatchedPair(leftWord, rightWord)
            } else {
                _baseState.update {
                    it.copy(
                        lives = it.lives.dec(),
                        isLessonFailed = it.lives.dec() == 0
                    )
                }
                clearSelect()
            }
        } else {
            Log.e(
                "MatchLessonVM",
                "compareSelectedWords selectedWordUI = null, leftWord = $leftWord"
            )
        }
    }

    private fun removeMatchedPair(leftWord: MatchViewData, rightWord: MatchViewData) {
        if (leftWord is MatchViewData.MatchWordData && rightWord is MatchViewData.MatchWordData) {
            val newLeftColumn = _state.value?.leftColumn?.map {
                if (it.id == leftWord.id) {
                    leftWord.copy(isMatched = true)
                } else it
            } ?: emptyList()
            val newRightColumn = _state.value?.rightColumn?.map {
                if (it.id == rightWord.id) {
                    rightWord.copy(isMatched = true)
                } else it
            } ?: emptyList()

            val newState = _state.value?.copy(
                rightColumn = newRightColumn.toList(),
                leftColumn = newLeftColumn.toList()
            )
            _state.update { newState }
        }
        val newLeftColumn = _state.value?.leftColumn?.map {
            if (it.id == leftWord.id) {
                MatchViewData.MatchEmpty(leftWord.id)
            } else it
        } ?: emptyList()
        val newRightColumn = _state.value?.rightColumn?.map {
            if (it.id == rightWord.id) {
                MatchViewData.MatchEmpty(rightWord.id)
            } else it
        } ?: emptyList()
        val newState = MatchLessonState(
            leftColumn = newLeftColumn.toList(),
            rightColumn = newRightColumn.toList()
        )
        _state.update { newState }
        checkEmpty()
    }

    private fun clearSelect() {
        val newLeftColumn = _state.value?.leftColumn?.map {
            when (it) {
                is MatchViewData.MatchWordData -> it.copy(isSelected = false)
                is MatchViewData.MatchEmpty -> it
            }
        } ?: emptyList()
        val newRightColumn = _state.value?.rightColumn?.map {
            when (it) {
                is MatchViewData.MatchWordData -> it.copy(isSelected = false)
                is MatchViewData.MatchEmpty -> it
            }
        } ?: emptyList()
        val newState = MatchLessonState(
            leftColumn = newLeftColumn.toList(),
            rightColumn = newRightColumn.toList()
        )
        _state.update { newState }
    }

    private fun checkEmpty() {
        viewModelScope.launch {
            updateWords()
            _baseState.update {
                it.copy(isLessonCompleted = _state.value?.leftColumn?.none { it is MatchViewData.MatchWordData } == true)
            }
        }
    }

    companion object {
        private const val TIMER = 30000L
    }
}

data class MatchLessonState(
    val leftColumn: List<MatchViewData>,
    val rightColumn: List<MatchViewData>
)