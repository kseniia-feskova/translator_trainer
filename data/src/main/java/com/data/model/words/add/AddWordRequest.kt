package com.data.model.words.add

import com.data.model.WordStatus
import java.util.UUID

data class AddWordRequest(
    val originalText: String,
    val translatedText: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val status: WordStatus = WordStatus.New,
    val courseId: UUID
)
