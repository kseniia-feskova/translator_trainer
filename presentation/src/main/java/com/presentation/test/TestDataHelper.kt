package com.presentation.test

import com.presentation.R
import com.presentation.model.CourseUI
import com.presentation.model.Level
import com.presentation.model.SetOfCards
import com.presentation.model.WordUI
import com.presentation.utils.Language
import java.util.UUID


// не самая полезная вещь, но вдруг нужны будут тестовые наборы, хотя они тут очень неудобно собираються
val smallList = listOf(
    WordUI(UUID.randomUUID(), "deutsches Wort", "немецкое слово", Level.getRandom()),
    WordUI(UUID.randomUUID(), "Katze", "Котик", Level.getRandom()),
    WordUI(UUID.randomUUID(), "Mutter", "Мама", Level.getRandom())
)

val mockSetOfCard = SetOfCards(
    UUID.randomUUID(),
    "Набор",
    isDefault = false,
    words = emptyList()
)

val mockListOfSets = listOf(
    mockSetOfCard,
    mockSetOfCard.copy(title = "Новые слова"),
    mockSetOfCard.copy(title = "Приключение к центру земли"),
    mockSetOfCard.copy(title = "Повторить"),
    mockSetOfCard.copy(title = "Example"),
    mockSetOfCard.copy(title = "Все сложные глаголы"),
    mockSetOfCard.copy(title = "Все слова"),
)

val dummyCourses = listOf(
    CourseUI(
        originalLanguage = Language.RUSSIAN,
        translateLanguage = Language.GERMAN,
        originalFlag = R.drawable.ic_ru,
        translatedFlag = R.drawable.ic_de,
        id = UUID.randomUUID().toString(),
        allWordsId = null,
        selectedSetId = null
    ),
    CourseUI(
        originalLanguage = Language.FRENCH,
        translateLanguage = Language.GERMAN,
        originalFlag = R.drawable.ic_fr,
        translatedFlag = R.drawable.ic_de,
        id = UUID.randomUUID().toString(),
        allWordsId = null,
        selectedSetId = null
    ),
    CourseUI(
        originalLanguage = Language.RUSSIAN,
        translateLanguage = Language.FRENCH,
        originalFlag = R.drawable.ic_ru,
        translatedFlag = R.drawable.ic_fr,
        id = UUID.randomUUID().toString(),
        allWordsId = null,
        selectedSetId = null
    )
)