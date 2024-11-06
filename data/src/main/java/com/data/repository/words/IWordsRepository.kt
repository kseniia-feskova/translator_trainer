package com.data.repository.words

import com.data.model.SetOfWords
import com.data.model.SetWordCrossRef
import com.data.model.WordEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface IWordsRepository {

    //IAddWordUseCase
    suspend fun addNewWord(newWord: WordEntity): Long

    //IAddSetOfWordsUseCase
    suspend fun insertSet(setOfWords: SetOfWords)

    //IGetSetOfWordsUseCase
    suspend fun getSetByName(name: String): SetOfWords?

    //IGetSetOfWordsUseCase
    suspend fun getSetById(id: Int): SetOfWords?

    //IGetWordsOfSetUseCase
    fun getWordsInSet(setId: Int): Flow<List<WordEntity>>

    //default in IAddWordUseCase
    suspend fun addWordToAllWordsSet(word: WordEntity)

    fun getWordsFilteredByDateOrStatus(
        startDate: Date,
        endDate: Date
    ): Flow<List<WordEntity>>

    suspend fun insertSetWordCrossRef(crossRef: SetWordCrossRef)
}