package com.presentation.test

import com.presentation.R
import com.presentation.model.Level
import com.presentation.model.SetLevel
import com.presentation.model.SetOfCards
import com.presentation.model.WordUI

// не самая полезная вещь, но вдруг нужны будут тестовые наборы, хотя они тут очень неудобно собираються
val smallList = listOf(
    WordUI(0, "deutsches Wort", "немецкое слово", Level.getRandom()),
    WordUI(1, "Katze", "Котик", Level.getRandom()),
    WordUI(2, "Mutter", "Мама", Level.getRandom()),
    WordUI(3, "Vater", "Отец, папа", Level.getRandom()),
    WordUI(4, "Spiegel", "Зеркало", Level.getRandom()),
    WordUI(5, "Sprache", "Язык, речь", Level.getRandom()),
    WordUI(6, "deutsches Wort", "немецкое слово", Level.getRandom()),
    WordUI(7, "Katze", "Котик", Level.getRandom()),
    WordUI(8, "Mutter", "Мама", Level.getRandom())
)

private val listOfResource: List<Int> = listOf(
    R.string.uncle,
    R.string.Hurry,
    R.string.house,
    R.string.district,
    R.string.herd,
    R.string.cook,
    R.string.impatient,
    R.string.cry,
    R.string.confusion,
    R.string.to,
    R.string.dining_room,
    R.string.beaten,
    R.string.breaks_away,
    R.string.supposedly,
    R.string.understanding,
    R.string.rushed,
    R.string.angry,
    R.string.hesitating,
    R.string.therefore,
    R.string.expression,
    R.string.smart,
    R.string.small_room,
    R.string.up,
    R.string.went_on_board,
    R.string.haustur,
    R.string.creaked,
    R.string.hausbern
)


val listOfDummyCards = listOf(
    WordUI(0, "Text", "Текст", Level.NEW),
    WordUI(1, "First word", "Первое слово", Level.NEW),
    WordUI(2, "Second word", "Второе слово", Level.NEW),
    WordUI(3, "Too loong word for translate", "Длинный текст для перевода", Level.NEW),
    WordUI(4, "And the other one", "И еще одно слово", Level.NEW)
)

val mockSetOfCard = SetOfCards(
    0,
    "Набор",
    SetLevel.EASY,
    emptySet(),
    1
)

val mockListOfSets = listOf(
    mockSetOfCard,
    mockSetOfCard.copy(title = "Новые слова"),
    mockSetOfCard.copy(title = "Приключение к центру земли", setOfWords = smallList.toSet()),
  //  mockSetOfCard.copy(title = "Набор для B2"),
    mockSetOfCard.copy(title = "Все слова"),
)