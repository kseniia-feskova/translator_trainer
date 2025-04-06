package translator.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import translator.data.model.sets.SetOfWords
import translator.data.model.sets.SetWithWords
import java.util.UUID

@Dao
interface SetsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSet(setOfWords: SetOfWords): Long

    @Transaction
    @Query("SELECT * FROM sets_of_words")
    suspend fun getAllSets(): List<SetWithWords>

    @Query("SELECT * FROM sets_of_words WHERE id = :id")
    suspend fun getSetById(id: UUID): SetWithWords?
}

const val ALL_WORDS = "Все слова"
