package com.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.model.Level
import com.presentation.model.SetLevel
import com.presentation.model.SetOfCards
import com.presentation.model.WordUI
import com.presentation.usecases.IAddSetOfWordsUseCase
import com.presentation.usecases.IAddSetWordCrossRefUseCase
import com.presentation.usecases.IGetSetOfCardsUseCase
import com.presentation.usecases.IGetWordsOfSetUseCase
import com.presentation.usecases.IUpdateWordUseCase
import com.presentation.utils.CURRENT_WORDS
import com.presentation.utils.Course
import com.presentation.utils.selectLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardSetViewModel(
    private val setId: Int,
    private val getCardSet: IGetSetOfCardsUseCase,
    private val gerWords: IGetWordsOfSetUseCase,
    private val updateWord: IUpdateWordUseCase,
    private val addSet: IAddSetOfWordsUseCase,
    private val addWordsToSet: IAddSetWordCrossRefUseCase
) : ViewModel() {

    private var listOfCards: List<WordUI> = listOf()
    private var setOfCards: SetOfCards? = null
    private var setOFWords: Set<WordUI> = setOf()
    private val _uiState = MutableStateFlow(CardSetState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            setOfCards = getCardSet.invoke(setId)
            Log.e(TAG, "set = ${setOfCards?.title}, id = $setId")
            setOfCards?.run {
                if (id != null) {
                    val words = gerWords.invoke(id).first()
                    setOFWords = words.toSet()
                    Log.e(TAG, "Words = ${words.size}, set = ${title}")
                    if (listOfCards.isEmpty()) {
                        listOfCards = words
                        _uiState.update {
                            it.copy(
                                name = title,
                                allWords = words.size,
                                knowWords = words.filter { it.level == Level.KNOW }.size,
                                words = Pair(listOfCards.first(), listOfCards.getOrNull(1))
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                knowWords = words.filter { it.level == Level.KNOW }.size,
                            )
                        }
                    }
                }
            }
        }
    }

    fun handleIntent(intent: CardSetIntent) {
        when (intent) {
            is CardSetIntent.AddWordToKnow -> addWordToKnow(word = intent.word)
            is CardSetIntent.AddWordToLearn -> addWordToLearn(word = intent.word)
            is CardSetIntent.ResetCardSet -> resetCardSet()
            is CardSetIntent.StartSelected -> {
                viewModelScope.launch {
                    val currentSet = getCardSet.invoke(CURRENT_WORDS)
                    Log.e(TAG, "Current = $currentSet")
                    if (currentSet?.id == null) {
                        createCurrentSet { setId, course -> intent.onSetCreated(setId, course) }
                    } else {
                        val words = gerWords.invoke(currentSet.id).first()
                        intent.onSetCreated(currentSet.id, words.selectLevel())
                    }
                }
            }
        }
    }

    private fun addWordToKnow(word: WordUI) {
        val temp = listOfCards.toMutableList()
        val oldValueIndex = temp.indexOf(word)
        if (oldValueIndex != -1) {
            temp[oldValueIndex] = word.copy(level = Level.KNOW)
            viewModelScope.launch {
                updateWord.invoke(temp[oldValueIndex])
            }
            listOfCards = temp
            updateUI()
        }
    }

    private fun addWordToLearn(word: WordUI) {
        val temp = listOfCards.toMutableList()
        val oldValueIndex = temp.indexOf(word)
        if (oldValueIndex != -1) {
            temp[oldValueIndex] = word.copy(level = Level.NEW)
            viewModelScope.launch {
                updateWord.invoke(temp[oldValueIndex])
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
        } else {
            _uiState.update { state ->
                state.copy(
                    knowWords = listOfCards.filter { it.level == Level.KNOW }.size,
                    words = null
                )
            }
        }
    }

    private fun createCurrentSet(onSetCreated: (Int, Course) -> Unit) {
        viewModelScope.launch {
            setOfCards?.run {
                if (id != null) {
                    val current = SetOfCards(
                        title = CURRENT_WORDS,
                        level = SetLevel.EASY,
                        userId = userId
                    )
                    val setID = addSet.invoke(current)
                    if (setID != -1L) {
                        Log.e(TAG, "create set = $setID")
                        val words = gerWords.invoke(id).first()
                        val filtered =
                            words.filter { it.level != Level.KNOW }.shuffled()
                        if (filtered.size > 5) {
                            val selected = filtered.take(5)
                            selected.forEach {
                                addWordsToSet.invoke(wordID = it.id, setID = setID.toInt())
                            }
                            Log.e(TAG, "selected = $selected")
                            onSetCreated(setID.toInt(), selected.selectLevel())
                        } else {
                            filtered.forEach {
                                addWordsToSet.invoke(wordID = it.id, setID = setID.toInt())
                            }
                            Log.e(TAG, "filtered = $filtered")
                            onSetCreated(setID.toInt(), filtered.selectLevel())
                        }
                    }
                }
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

    companion object {
        private const val TAG = "CardSetViewModel"
    }
}

data class CardSetState(
    val knowWords: Int = 10,
    val allWords: Int = 27,
    val name: String = "Набор",
    val words: Pair<WordUI, WordUI?>? = null,
)

sealed class CardSetIntent {
    data class AddWordToKnow(val word: WordUI) : CardSetIntent()
    data class AddWordToLearn(val word: WordUI) : CardSetIntent()
    object ResetCardSet : CardSetIntent()
    data class StartSelected(val onSetCreated: (Int, Course) -> Unit = { _, _ -> }) :
        CardSetIntent()
}
