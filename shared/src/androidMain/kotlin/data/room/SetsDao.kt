package data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import data.room.model.SetOfWords
import data.room.model.SetWithWords
import kotlinx.coroutines.flow.Flow

@Dao
interface SetsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSet(setOfWords: SetOfWords): Long

    @Transaction
    @Query("SELECT * FROM sets_of_words")
    suspend fun getAllSets(): List<SetWithWords>

    @Query("SELECT * FROM sets_of_words")
    fun observeSets(): Flow<List<SetWithWords>>

    @Query("SELECT * FROM sets_of_words WHERE id = :id")
    suspend fun getSetById(id: String): SetWithWords?
}

