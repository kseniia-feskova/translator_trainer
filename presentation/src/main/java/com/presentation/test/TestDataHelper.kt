package com.presentation.test

import com.presentation.model.Level
import com.presentation.model.SetOfCards
import com.presentation.model.WordUI
import java.util.UUID


// не самая полезная вещь, но вдруг нужны будут тестовые наборы, хотя они тут очень неудобно собираються
val smallList = listOf(
    WordUI(UUID.fromString("0"), "deutsches Wort", "немецкое слово", Level.getRandom()),
    WordUI(UUID.fromString("1"), "Katze", "Котик", Level.getRandom()),
    WordUI(UUID.fromString("2"), "Mutter", "Мама", Level.getRandom()),
    WordUI(UUID.fromString("3"), "Vater", "Отец, папа", Level.getRandom()),
    WordUI(UUID.fromString("4"), "Spiegel", "Зеркало", Level.getRandom()),
    WordUI(UUID.fromString("5"), "Sprache", "Язык, речь", Level.getRandom()),
    WordUI(UUID.fromString("6"), "deutsches Wort", "немецкое слово", Level.getRandom()),
    WordUI(UUID.fromString("7"), "Katze", "Котик", Level.getRandom()),
    WordUI(UUID.fromString("8"), "Mutter", "Мама", Level.getRandom())
)

val mockSetOfCard = SetOfCards(
    UUID.fromString("0"),
    "Набор",
    isDefault = false,
    emptySet(),
    UUID.fromString("1")
)

val mockListOfSets = listOf(
    mockSetOfCard,
    mockSetOfCard.copy(title = "Новые слова"),
    mockSetOfCard.copy(title = "Приключение к центру земли", setOfWords = smallList.toSet()),
    mockSetOfCard.copy(title = "Все слова"),
)