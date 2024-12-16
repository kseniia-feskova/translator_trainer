package com.data.repository.words

import com.data.model.SetOfWords
import com.data.model.SetWithWords
import com.data.model.SetWordCrossRef
import com.data.model.WordEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface IWordsRepository {

    //IAddWordUseCase
    suspend fun addNewWord(newWord: WordEntity): Long

    //IAddSetOfWordsUseCase
    suspend fun insertSet(setOfWords: SetOfWords): Long

    //IGetSetOfWordsUseCase
    suspend fun getSetByName(name: String): SetOfWords?

    //IGetSetOfWordsUseCase
    suspend fun getSetById(id: Int): SetOfWords?

    //IGetWordsOfSetUseCase
    fun getWordsInSet(setId: Int): Flow<SetWithWords>

    //default in IAddWordUseCase
    suspend fun addWordToAllWordsSet(word: WordEntity)

    fun getWordsFilteredByDateOrStatus(
        startDate: Date,
        endDate: Date
    ): Flow<List<WordEntity>>

    suspend fun insertSetWordCrossRef(crossRef: SetWordCrossRef)

    suspend fun updateWord(newWord: WordEntity)

    suspend fun getWordById(wordId: Int): WordEntity?

    suspend fun deleteSetById(setId: Int)

    suspend fun findWordByOrigin(origin: String): WordEntity?

    suspend fun findWordByTranslated(translated: String): WordEntity?

    suspend fun getAllSets(): List<SetWithWords>

    suspend fun deleteWord(wordId: Int)

}