package presentation.ui.screens.lesson.dictation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import domain.usecases.sets.IUpdateSetsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.cache.IUiDataManager
import presentation.model.LessonType
import presentation.model.WordResult
import presentation.model.WordUI
import presentation.ui.screens.lesson.base.BaseLessonState
import presentation.ui.screens.lesson.base.BaseLessonViewModel
import domain.usecases.words.IGetWordsBySetUseCase
import domain.usecases.words.IUpdateStatusUseCase

class DictationLessonViewModel(
    savedStateHandle: SavedStateHandle,
    getWordsBySetUseCase: IGetWordsBySetUseCase,
    updateStatusUseCase: IUpdateStatusUseCase,
    updateSetsUseCase: IUpdateSetsUseCase,
    private val uiDataManager: IUiDataManager
) : BaseLessonViewModel(
    savedStateHandle,
    getWordsBySetUseCase,
    updateStatusUseCase,
    updateSetsUseCase
) {
    private val _state = MutableStateFlow<DictationLessonState?>(null)
    val state = _state.asStateFlow()

    override val _baseState = MutableStateFlow(BaseLessonState(time = TIMER, lives = 0))
    val baseState = _baseState.asStateFlow()

    override fun checkIfLessonCompleted(): Boolean {
        val isCompleted = translatedList.isNotEmpty() && translatedList.size == words.size
        return isCompleted
    }

    override fun reload(time: Long) {
        super.reload(TIMER)
        setWords()
    }

    private val translatedList = mutableMapOf<WordUI, String>()
    private var wordIndex = 0

    init {
        startTheTimer()
        initializeWords {
            setWords()
        }
    }

    fun navigateToSuccess(navigate: (LessonType) -> Unit) {
        super.navigateToSuccess()
        navigate(LessonType.DICTATION)
    }

    fun onValueChange(newInput: String) {
        _state.update {
            it?.copy(inputText = newInput)
        }
    }

    fun onSkip() {
        val state = _state.value
        if (state != null) {
            wordIndex++
            recountIndex()
            _state.update {
                it?.copy(currentWord = words[wordIndex], inputText = "")
            }
        }
    }

    fun onTranslate() {
        val state = _state.value
        if (state != null) {
            translatedList[state.currentWord] = state.inputText
            if (checkIfLessonCompleted()) {
                val correctTranslations = translatedList.mapNotNull {
                    if (it.value == it.key.resText) {
                        it.key
                    } else null
                }
                viewModelScope.launch {
                    uiDataManager.saveWordsTranslation(translatedList.map {
                        WordResult(
                            it.key,
                            it.value
                        )
                    })
                    updateWords(correctTranslations)
                }
                _baseState.update { it.copy(isLessonCompleted = true) }
            } else {
                recountIndex()
                _state.update {
                    it?.copy(
                        currentWord = words[wordIndex],
                        currentWordNumber = translatedList.size,
                        inputText = ""
                    )
                }
            }
        }
    }

    private fun recountIndex() {
        if (wordIndex == words.size) {
            wordIndex = 0
        }
        while (translatedList.keys.contains(words[wordIndex])) {
            wordIndex++
            if (wordIndex == words.size) {
                wordIndex = 0
            }
        }
    }

    private fun setWords() {
        _state.update {
            DictationLessonState(
                currentWord = words.first(),
                countOfWords = words.size,
                currentWordNumber = 0
            )
        }
        translatedList.clear()
        wordIndex = 0
    }


    companion object {
        private const val TIMER = (1000 * 40L)
    }
}