package com.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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