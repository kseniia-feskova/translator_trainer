package com.data.mock.repo

import com.data.mock.model.SetOfCards
import com.data.mock.model.Word
import kotlinx.coroutines.flow.StateFlow

interface IMockWordRepository {

    val setsFlow: StateFlow<List<SetOfCards>>

    fun addNewWord(setId: Int, newWord: Word)

    fun getSetOfWords(setId: Int): SetOfCards
}