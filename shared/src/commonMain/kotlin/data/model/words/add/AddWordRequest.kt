package data.model.words.add

import data.model.words.WordStatus
import kotlinx.serialization.Serializable

@Serializable
data class AddWordRequest(
    val originalText: String,
    val translatedText: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val status: WordStatus = WordStatus.New,
    val courseId: String
)
