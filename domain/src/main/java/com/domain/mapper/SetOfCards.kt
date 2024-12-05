package com.domain.mapper

import com.data.model.SetOfWords
import com.data.model.SetWithWords
import com.presentation.model.SetLevel
import com.presentation.model.SetOfCards

fun SetOfCards.toData(): SetOfWords {
    return SetOfWords(
        name = title,
        level = level.toData(),
        userId = userId,

    )
}

fun SetLevel.toData(): com.data.model.SetLevel {
    return when (this) {
        SetLevel.EASY -> com.data.model.SetLevel.EASY
        SetLevel.MEDMIUM -> com.data.model.SetLevel.MEDIUM
        SetLevel.HARD -> com.data.model.SetLevel.HARD
    }
}


fun SetOfWords.toPresentation(): SetOfCards {
    return SetOfCards(
        id = id,
        title = name,
        level = level.toPresentation(),
        userId = userId
    )
}

fun SetWithWords.toPresentation(): SetOfCards {
    return SetOfCards(
        id = set.id,
        title = set.name,
        level = set.level.toPresentation(),
        userId = set.userId,
        setOfWords = words.map { it.toWord() }.toSet()
    )
}

fun com.data.model.SetLevel.toPresentation(): SetLevel {
    return when (this) {
        com.data.model.SetLevel.EASY -> SetLevel.EASY
        com.data.model.SetLevel.MEDIUM -> SetLevel.MEDMIUM
        com.data.model.SetLevel.HARD -> SetLevel.HARD
    }
}