package com.presentation.model

import java.util.UUID

// пока хз
data class SetOfCards(
    val id: UUID,
    val title: String,
    val isDefault: Boolean,
    val learnedWords: Int = 0,
    val allWordsCount: Int = 0,
    val courseId: UUID
)

enum class SetLevel {
    EASY,
    MEDMIUM,
    HARD
}
