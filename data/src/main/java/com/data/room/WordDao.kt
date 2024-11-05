package com.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.data.model.SetOfWords
import com.data.model.SetWordCrossRef
import com.data.model.WordEntity
import com.data.model.WordStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWord(word: WordEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSet(setOfWords: SetOfWords): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetWordCrossRef(crossRef: SetWordCrossRef)

    @Transaction
    @Query("SELECT * FROM sets_of_words WHERE name = :name")
    suspend fun getSetByName(name: String): SetOfWords?

    @Transaction
    @Query("SELECT * FROM words WHERE id IN (SELECT wordId FROM set_word_cross_ref WHERE setId = :setId)")
    fun getWordsInSet(setId: Int): Flow<List<WordEntity>>

    // Добавить слово в набор "Все слова"
    suspend fun addWordToAllWordsSet(word: WordEntity) {
        val allWordsSet = getSetByName("Все слова") ?: return
        val wordId = insertWord(word)
        if (wordId != -1L) {
            insertSetWordCrossRef(SetWordCrossRef(setId = allWordsSet.id, wordId = wordId.toInt()))
        }
    }

    // Получить слова по фильтру даты или статуса
    @Transaction
    @Query("SELECT * FROM words WHERE dateAdded BETWEEN :startDate AND :endDate OR status = :status")
    fun getWordsFilteredByDateOrStatus(
        startDate: Date,
        endDate: Date,
        status: WordStatus
    ): Flow<List<WordEntity>>
}
