package com.presentation.model

// пока хз
data class SetOfCards(
    val id: Int? = null,
    val title: String,
    val level: SetLevel,
    val setOfWords: Set<WordUI> = emptySet(),
    val userId: Int
)

enum class SetLevel {
    EASY,
    MEDMIUM,
    HARD
}