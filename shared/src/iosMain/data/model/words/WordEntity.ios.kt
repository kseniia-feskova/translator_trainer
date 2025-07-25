package data.model.words

import data.model.words.WordStatus

actual class WordEntity actual constructor(
    val id: String,
    val originalText: String,
    val translatedText: String,
    val status: data.model.words.WordStatus,
    val sourceLanguage: String,
    val targetLanguage: String,
    val courseId: String
)