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
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MainViewModel(
    private val getSet: IGetSetOfAllCardsUseCase,
    private val addSet: IAddSetOfWordsUseCase,
    private val getNewWords: IGetFilteredWordsUseCase,
    private val addWordToSet: IAddSetWordCrossRef
) : ViewModel() {

    fun checkAndAddAllWordsSet() {
        viewModelScope.launch {
            val currentSet = getSet.invoke()
            if (currentSet == null) {
                addSet.invoke(allSetCards)
            } else {
                val cards = getNewWords.getWordsFilteredByDateOrStatus(
                    Date(),
                    getPreviousDay()
                ).last()
                Log.e("MainViewModel", "New words = $cards")
                val newCradsSet = SetOfWords(
                    currentSet.id + 1,
                    NEW_WORDS,
                    SetLevel.EASY,
                    0
                )
                cards.forEach {
                    addWordToSet.invoke(wordID = it.id, setID = newCradsSet.id)
                }
            }
        }
    }

    private fun getPreviousDay(): Date {
        val calendar = Calendar.getInstance()  // Получаем текущую дату
        calendar.add(Calendar.DAY_OF_YEAR, -1) // Отнимаем один день
        return calendar.time                   // Получаем дату предыдущего дня
    }

    companion object {
        private val allSetCards = SetOfWords(0, ALL_WORDS, SetLevel.EASY, 0)
    }
}