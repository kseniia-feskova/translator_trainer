package com.data.repository.words

import com.data.model.SetOfWords
import com.data.model.SetWithWords
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

    override suspend fun insertSet(setOfWords: SetOfWords): Long {
        return dao.insertSet(setOfWords)
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

    override fun getWordsInSet(setId: Int): Flow<SetWithWords> {
        return dao.getSetWithWords(setId)
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

    override suspend fun updateWord(newWord: WordEntity) {
        dao.updateWord(newWord)
    }

    override suspend fun getWordById(wordId: Int): WordEntity? {
        return dao.getWordById(wordId)
    }

    override suspend fun deleteSetById(setId: Int) {
        dao.deleteSetWithWords(setId)
    }

    override suspend fun findWordByOrigin(origin: String): WordEntity? {
        return dao.getWordByOriginal(origin)
    }

    override suspend fun findWordByTranslated(translated: String): WordEntity? {
        return dao.getWordByTranslated(translated)
    }

    override suspend fun getAllSets(): List<SetWithWords> {
        return dao.getAllSets()
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