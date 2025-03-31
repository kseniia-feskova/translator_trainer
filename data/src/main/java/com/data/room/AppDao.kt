package com.data.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AppDao {
    @Query("DELETE FROM sets_of_words")
    suspend fun clearUsers()

    @Query("DELETE FROM set_word_cross_ref")
    suspend fun clearSets()

    @Query("DELETE FROM words")
    suspend fun clearWords()

}
