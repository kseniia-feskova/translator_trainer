package com.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.data.model.SetOfWords
import com.data.model.SetWithWords
import com.data.model.SetWordCrossRef
import com.data.model.WordEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.UUID

const val ALL_WORDS = "Все слова"

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWord(word: WordEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSet(setOfWords: SetOfWords): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetWordCrossRef(crossRef: SetWordCrossRef)

    @Query("SELECT * FROM words WHERE LOWER(original) = LOWER(:original) LIMIT 1")
    suspend fun getWordByOriginal(original: String): WordEntity?

    @Query("SELECT * FROM words WHERE LOWER(translation) = LOWER(:translated) LIMIT 1")
    suspend fun getWordByTranslated(translated: String): WordEntity?

    @Transaction
    @Query("SELECT * FROM sets_of_words WHERE name = :name")
    suspend fun getSetByName(name: String): SetOfWords?

    @Query("SELECT * FROM sets_of_words WHERE id = :id")
    suspend fun getSetById(id: Int): SetOfWords?

    @Transaction
    @Query("SELECT * FROM sets_of_words")
    suspend fun getAllSets(): List<SetWithWords>

    @Transaction
    @Query("SELECT * FROM sets_of_words WHERE id = :setId")
    fun getSetWithWords(setId: Int): Flow<SetWithWords>

    // Добавить слово в набор "Все слова"
    suspend fun addWordToAllWordsSet(word: WordEntity) {
        val allWordsSet = getSetByName(ALL_WORDS) ?: return
        val wordId = insertWord(word)
        if (wordId != -1L) {
            insertSetWordCrossRef(SetWordCrossRef(setId = allWordsSet.id, wordId = word.id))
        }
    }

    suspend fun addWordToAllWordsSet(wordId: UUID) {
        val allWordsSet = getSetByName(ALL_WORDS) ?: return
        insertSetWordCrossRef(SetWordCrossRef(setId = allWordsSet.id, wordId = wordId))
    }

    // Получить слова по фильтру даты или статуса
    @Transaction
    @Query("SELECT * FROM words WHERE dateAdded BETWEEN :startDate AND :endDate")
    fun getWordsFilteredByDate(
        startDate: Date,
        endDate: Date,
    ): Flow<List<WordEntity>>

    // Метод для обновления слова
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWord(word: WordEntity)

    @Query("SELECT * FROM words WHERE id = :id")
    suspend fun getWordById(id: UUID): WordEntity?

    // Метод для удаления набора слов и связанных записей
    @Transaction
    suspend fun deleteSetWithWords(setId: Int) {
        // Удаляем связи между набором и словами
        deleteSetWordCrossRefs(setId)
        // Удаляем сам набор
        deleteSetById(setId)
    }

    // Удалить записи в set_word_cross_ref по setId
    @Query("DELETE FROM set_word_cross_ref WHERE setId = :setId")
    suspend fun deleteSetWordCrossRefs(setId: Int)

    // Удалить набор по id
    @Query("DELETE FROM sets_of_words WHERE id = :setId")
    suspend fun deleteSetById(setId: Int)



    @Transaction
    suspend fun deleteWordWithRelations(wordId: UUID) {
        deleteWordFromSets(wordId)
        deleteWordById(wordId)
    }
    @Query("DELETE FROM words WHERE id = :wordId")
    suspend fun deleteWordById(wordId: UUID)

    @Query("DELETE FROM set_word_cross_ref WHERE wordId = :wordId")
    suspend fun deleteWordFromSets(wordId: UUID)
}
