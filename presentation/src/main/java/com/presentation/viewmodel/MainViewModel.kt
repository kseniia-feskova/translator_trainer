package com.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.model.SetLevel
import com.presentation.model.SetOfCards
import com.presentation.usecases.IAddSetOfWordsUseCase
import com.presentation.usecases.IAddSetWordCrossRefUseCase
import com.presentation.usecases.IGetFilteredWordsUseCase
import com.presentation.usecases.IGetSetOfAllCardsUseCase
import com.presentation.usecases.IGetSetOfCardsUseCase
import com.presentation.utils.ALL_WORDS
import com.presentation.utils.NEW_WORDS
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MainViewModel(
    private val getAllCards: IGetSetOfAllCardsUseCase,
    private val getSet: IGetSetOfCardsUseCase,
    private val addSet: IAddSetOfWordsUseCase,
    private val getNewWords: IGetFilteredWordsUseCase,
    private val addWordToSet: IAddSetWordCrossRefUseCase
) : ViewModel() {

    //создание массивов, если их нет
    fun checkAndAddAllWordsSet() {
        viewModelScope.launch {
            val allCards = getAllCards.invoke()
            if (allCards == null) {
                addSet.invoke(allSetCards)
            } else {
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