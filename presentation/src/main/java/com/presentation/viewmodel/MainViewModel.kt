package com.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import com.presentation.model.SetLevel
import com.presentation.model.SetOfCards
import com.presentation.usecases.IAddSetOfWordsUseCase
import com.presentation.usecases.IAddSetWordCrossRefUseCase
import com.presentation.usecases.IDeleteSetByIdUseCase
import com.presentation.usecases.words.IGetFilteredWordsUseCase
import com.presentation.usecases.IGetSetOfAllCardsUseCase
import com.presentation.usecases.IGetSetOfCardsUseCase
import com.presentation.utils.ALL_WORDS
import com.presentation.utils.CURRENT_WORDS
import com.presentation.utils.NEW_WORDS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MainViewModel(
    private val getAllCards: IGetSetOfAllCardsUseCase,
    private val getSet: IGetSetOfCardsUseCase,
    private val addSet: IAddSetOfWordsUseCase,
    private val getNewWords: IGetFilteredWordsUseCase,
    private val addWordToSet: IAddSetWordCrossRefUseCase,
    private val deleteSet: IDeleteSetByIdUseCase,
    private val dataStorage: IDataStoreManager
) : ViewModel() {

    private val _isUserAuthorized = MutableStateFlow(false)
    val isUserAuthorized: StateFlow<Boolean> = _isUserAuthorized.asStateFlow()

    init {
        observeUserId()
    }

    private fun observeUserId() {
        viewModelScope.launch {
            dataStorage.listenUserId()
                .map { userId -> userId != null } // true, если userId не null
                .distinctUntilChanged() // Отслеживаем только изменения
                .collect { isAuthorized ->
                    Log.e("MainViewModel", "IsAuthorized = $isAuthorized")
                    _isUserAuthorized.value = isAuthorized
                }
        }
    }
    //создание массивов, если их нет
    fun checkAndAddAllWordsSet() {
        viewModelScope.launch {
            val allCards = getAllCards.invoke()
            if (allCards == null) {
                addSet.invoke(allSetCards)
            } else {
                launch {
                    getNewWords.getWordsFilteredByDateOrStatus(
                        getPreviousDay(),
                        Date()
                    ).collect { cards ->
                        val newWords = getSet.invoke(NEW_WORDS)
                        if (newWords == null) {
                            Log.e("MainViewModel", "New words = $cards")
                            val newCardsSet = SetOfCards(
                                title = NEW_WORDS,
                                level = SetLevel.EASY,
                                userId = allCards.userId
                            )
                            val setId = addSet.invoke(newCardsSet)
                            if (setId != -1L) {
                                Log.e("NEW CARDS", "Response = $setId")
                                cards.forEach {
                                    addWordToSet.invoke(wordID = it.id, setID = setId.toInt())
                                }
                            }
                        }
                    }
                }

                val currentSet = getSet.invoke(CURRENT_WORDS)
                Log.e("MainViewModel", "Current = $currentSet")
                if (currentSet?.id != null && currentSet.setOfWords.isEmpty()) {
                    deleteSet.invoke(currentSet.id)
                }
            }
        }
    }

    companion object {
        val allSetCards = SetOfCards(0, ALL_WORDS, SetLevel.EASY, emptySet(), 0)
    }
}

fun getPreviousDay(): Date {
    val calendar = Calendar.getInstance()  // Получаем текущую дату
    calendar.add(Calendar.DAY_OF_YEAR, -1) // Отнимаем один день
    return calendar.time                   // Получаем дату предыдущего дня
}