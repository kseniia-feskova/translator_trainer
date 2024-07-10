package com.data.room

import androidx.room.*
import com.data.data.WordEntity
import io.reactivex.Observable

@Dao
interface HistoryDAO {

    @Query("SELECT * FROM wordentity")
    fun getAllObservable(): Observable<List<WordEntity>>

    @Query("SELECT * FROM wordentity WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<WordEntity>

    @Query("SELECT * FROM wordentity WHERE word LIKE :word  LIMIT 1")
    fun findByWord(word: String): WordEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg words: WordEntity)

    @Delete
    fun delete(word: WordEntity)

    @Query("SELECT * FROM wordentity")
    fun getAll(): List<WordEntity>

    @Query("SELECT * FROM wordentity ORDER BY id DESC LIMIT :count")
    fun getLastWords(count: Int): Observable<List<WordEntity>>
}