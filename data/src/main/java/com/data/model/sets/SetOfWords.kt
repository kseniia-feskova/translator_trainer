package com.data.model.sets

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.data.model.WordEntity
import java.util.UUID

@Entity(tableName = "sets_of_words")
data class SetOfWords(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String,
    val isDefault: Boolean,
    val words: List<String> = emptyList()
)

enum class SetLevel {
    EASY,
    MEDIUM,
    HARD
}

@Entity(
    tableName = "set_word_cross_ref",
    primaryKeys = ["setId", "wordId"],
    indices = [Index(value = ["wordId"])]
)
data class SetWordCrossRef(
    val setId: UUID,
    val wordId: UUID
)

data class SetWithWords(
    @Embedded val set: SetOfWords,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SetWordCrossRef::class,
            parentColumn = "setId",
            entityColumn = "wordId"
        )
    )
    val words: List<WordEntity>
)