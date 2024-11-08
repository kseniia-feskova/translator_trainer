package com.example.translatortrainer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.SetLevel
import com.data.model.SetOfWords
import com.data.room.ALL_WORDS
import com.data.room.NEW_WORDS
import com.domain.usecase.IAddSetOfWordsUseCase
import com.domain.usecase.IAddSetWordCrossRef
import com.domain.usecase.IGetFilteredWordsUseCase
import com.domain.usecase.IGetSetOfAllCardsUseCase
import com.domain.usecase.IGetSetOfCardsUseCase
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MainViewModel(
    private val getAllCards: IGetSetOfAllCardsUseCase,
    private val getSet: IGetSetOfCardsUseCase,
    private val addSet: IAddSetOfWordsUseCase,
    private val getNewWords: IGetFilteredWordsUseCase,
    private val addWordToSet: IAddSetWordCrossRef
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
                        val newCradsSet = SetOfWords(
                            name = NEW_WORDS,
                            level = SetLevel.EASY,
                            userId = allCards.userId
                        )
                        val setId = addSet.invoke(newCradsSet)
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
        private val allSetCards = SetOfWords(0, ALL_WORDS, SetLevel.EASY, 0)
    }
}

fun getPreviousDay(): Date {
    val calendar = Calendar.getInstance()  // Получаем текущую дату
    calendar.add(Calendar.DAY_OF_YEAR, -1) // Отнимаем один день
    return calendar.time                   // Получаем дату предыдущего дня
}