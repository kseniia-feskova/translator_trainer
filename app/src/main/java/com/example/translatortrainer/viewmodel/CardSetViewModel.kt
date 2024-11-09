package com.example.translatortrainer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.SetLevel
import com.data.model.SetOfWords
import com.data.model.WordEntity
import com.data.model.WordStatus
import com.data.room.CURRENT_WORDS
import com.domain.usecase.IAddSetOfWordsUseCase
import com.domain.usecase.IAddSetWordCrossRef
import com.domain.usecase.IGetSetOfCardsUseCase
import com.domain.usecase.IGetWordsOfSetUseCase
import com.domain.usecase.IUpdateWordUseCase
import com.example.translatortrainer.mapper.toWordUI
import com.example.translatortrainer.test.model.Level
import com.example.translatortrainer.test.model.WordUI
import com.example.translatortrainer.test.model.toWordEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardSetViewModel(
    private val setId: Int,
    private val getCardSet: IGetSetOfCardsUseCase,
    private val gerWords: IGetWordsOfSetUseCase,
    private val updateWord: IUpdateWordUseCase,
    private val addSet: IAddSetOfWordsUseCase,
    private val addWordsToSet: IAddSetWordCrossRef
) : ViewModel() {

    private var listOfCards: List<WordUI> = listOf()
    private var setOfCards: SetOfWords? = null
    private var setOFWords: Set<WordEntity> = setOf()
    private val _uiState = MutableStateFlow(CardSetState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            setOfCards = getCardSet.invoke(setId)
            Log.e(TAG, "set = ${setOfCards?.name}, id = $setId")
            setOfCards?.let { set ->
                gerWords.invoke(set.id).collect { words ->
                    setOFWords = words.toSet()
                    Log.e(TAG, "Words = ${words.size}, set = ${set.name}")
                    if (listOfCards.isEmpty()) {
                        listOfCards = words.map { word -> word.toWordUI() }
                        _uiState.update {
                            it.copy(
                                name = set.name,
                                allWords = words.size,
                                knowWords = words.filter { it.status == WordStatus.LEARNED }.size,
                                words = Pair(listOfCards.first(), listOfCards.getOrNull(1))
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                knowWords = words.filter { it.status == WordStatus.LEARNED }.size,
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
                createCurrentSet() { intent.onSetCreated(it) }
            }
        }
    }

    private fun addWordToKnow(word: WordUI) {
        val temp = listOfCards.toMutableList()
        val oldValueIndex = temp.indexOf(word)
        if (oldValueIndex != -1) {
            temp[oldValueIndex] = word.copy(level = Level.KNOW)
            viewModelScope.launch {
                updateWord.invoke(temp[oldValueIndex].toWordEntity())
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
                updateWord.invoke(temp[oldValueIndex].toWordEntity())
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

    private fun createCurrentSet(onSetCreated: (Int) -> Unit) {
        viewModelScope.launch {
            setOfCards?.let {
                val current = SetOfWords(
                    name = CURRENT_WORDS,
                    level = SetLevel.EASY,
                    userId = it.userId
                )
                val setID = addSet.invoke(current)
                if (setID != -1L) {
                    Log.e("NEW CARDS", "Response = $setId")
                    val filtered = setOFWords.filter { it.status != WordStatus.LEARNED }.shuffled()
                    if (filtered.size > 5) {
                        filtered.take(5).forEach {
                            addWordsToSet.invoke(wordID = it.id, setID = setID.toInt())
                        }
                    } else {
                        filtered.forEach {
                            addWordsToSet.invoke(wordID = it.id, setID = setID.toInt())
                        }
                    }
                    onSetCreated(setID.toInt())
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
    data class StartSelected(val onSetCreated: (Int) -> Unit = {}) : CardSetIntent()
}
