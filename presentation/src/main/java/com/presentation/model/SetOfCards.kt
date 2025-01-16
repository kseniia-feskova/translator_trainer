package com.presentation.model

import java.util.UUID

// пока хз
data class SetOfCards(
    val id: UUID,
    val title: String,
    val isDefault: Boolean,
    //val level: SetLevel,
    val setOfWords: Set<WordUI> = emptySet(),
    val courseId: UUID
)

enum class SetLevel {
    EASY,
    MEDMIUM,
    HARD
}