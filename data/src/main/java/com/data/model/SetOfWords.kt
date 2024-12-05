package com.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "sets_of_words")
data class SetOfWords(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val level: SetLevel,
    val userId: Int // Внешний ключ на пользователя
)

enum class SetLevel {
    EASY,
    MEDIUM,
    HARD
}

@Entity(
    tableName = "set_word_cross_ref",
    primaryKeys = ["setId", "wordId"]
)
data class SetWordCrossRef(
    val setId: Int,
    val wordId: Int
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