package com.data.repository.words

import com.data.model.SetOfWords
import com.data.model.SetWordCrossRef
import com.data.model.WordEntity
import com.data.room.WordDao
import kotlinx.coroutines.flow.Flow
import java.util.Date

class WordsRepository(
    private val dao: WordDao
) : IWordsRepository {

    override suspend fun addNewWord(newWord: WordEntity): Long {
        return dao.insertWord(newWord)
    }

    override suspend fun insertSet(setOfWords: SetOfWords) {
        dao.insertSet(setOfWords)
    }

    override suspend fun getSetByName(name: String): SetOfWords? {
        return dao.getSetByName(name)
    }

    override suspend fun getSetById(id: Int): SetOfWords? {
        return dao.getSetById(id)
    }

    override suspend fun addWordToAllWordsSet(word: WordEntity) {
        dao.addWordToAllWordsSet(word)
    }

    override fun getWordsInSet(setId: Int): Flow<List<WordEntity>> {
        return dao.getWordsInSet(setId)
    }

    override fun getWordsFilteredByDateOrStatus(
        startDate: Date,
        endDate: Date
    ): Flow<List<WordEntity>> {
        return dao.getWordsFilteredByDate(startDate, endDate)
    }

    override suspend fun insertSetWordCrossRef(crossRef: SetWordCrossRef) {
        dao.insertSetWordCrossRef(crossRef)
    }
}


/*
*   UseCases
*   1. GetLastWords - 5 new words
*   2. GetPageOfWords - 5-10 words?
*   3. AddNewWord -
*   4. TranslateWord - get Translation
*   5. GetExercise - get Exercise by type
*   6. DeleteWord
* */

/*
*  ExerciseType{
*   MAPPING, (word - translation selection)
*   DICTATION, (word - translation write)
*   MEMORIZE (word - translation rotation)
* }
*
* Exercise{
* map (word - translation),
* type : ExerciseType,
* }
*
* WordEntity{
* exercises: map (type, passed)
* }
*
* map (type, passed)
* */