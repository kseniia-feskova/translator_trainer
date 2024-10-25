package com.data.repository.words

import com.data.model.WordEntity
import com.data.room.HistoryDAO
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordsRepository(
    private val dao: HistoryDAO
) : IWordsRepository {

    override suspend fun addNewWord(newWord: WordEntity) = withContext(Dispatchers.IO) {
        dao.insertAll(newWord)
    }

    override fun getLastWord(count: Int): Observable<List<WordEntity>> {
        return dao.getLastWords(count)
    }

    override suspend fun deleteWord(word: WordEntity) = withContext(Dispatchers.IO) {
        dao.delete(word)
    }

    override suspend fun deleteById(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteById(id)
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