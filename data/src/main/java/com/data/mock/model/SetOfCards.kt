package com.data.mock.model

import com.data.model.SetLevel

data class SetOfCards(
    val id: Int,
    val title: String,
    val level: SetLevel,
    val setOfWords: Set<Word>
)