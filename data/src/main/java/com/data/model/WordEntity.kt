package com.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val original: String,
    val translation: String,
    val status: WordStatus,
    val dateAdded: Date = Date() // по умолчанию дата текущая
)

enum class WordStatus {
    NEW,
    IN_PROGRESS,
    LEARNED
}
