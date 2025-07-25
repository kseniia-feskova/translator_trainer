package data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import data.model.words.WordStatus
import java.util.UUID

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val originalText: String,
    val translatedText: String,
    val status: WordStatus,
    val sourceLanguage: String,
    val targetLanguage: String,
    val courseId: String
)

fun WordEntity.toCommon(): data.model.words.WordEntity {
    return data.model.words.WordEntity(
        id = id,
        originalText = originalText,
        translatedText = translatedText,
        status = status,
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
        courseId = courseId
    )
}

fun data.model.words.WordEntity.toAndroid(): WordEntity {
    return WordEntity(
        id = id,
        originalText = originalText,
        translatedText = translatedText,
        status = status,
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
        courseId = courseId
    )
}


