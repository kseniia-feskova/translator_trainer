package com.data.model.words

import com.data.model.UserEntity
import com.data.model.WordStatus
import java.util.UUID

data class AddWordResponse(
    val id: UUID,
    val originalText: String,
    val translatedText: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val user: UserEntity,
    val status: WordStatus = WordStatus.New,
)
