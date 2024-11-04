package com.data.mock.model

data class SetOfCards(
    val id: String,
    val title: String,
    val level: SetLevel,
    val setOfWords: Set<Word>
)