package com.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val originalText: String,
    val translatedText: String,
    val status: WordStatus,
)


enum class WordStatus {
    New, Learning, GoodLearning, Known;

    fun inc(): WordStatus {
        return when (this) {
            New -> Learning
            Learning -> GoodLearning
            GoodLearning -> Known
            Known -> Known
        }
    }
}
