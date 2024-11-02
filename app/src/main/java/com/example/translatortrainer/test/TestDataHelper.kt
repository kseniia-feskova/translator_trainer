package com.example.translatortrainer.test

import com.example.translatortrainer.R
import com.example.translatortrainer.test.model.Level
import com.example.translatortrainer.test.model.WordUI

interface ITestDataHelper {
    fun getListOfWords(): List<WordUI>
    fun updateList(newList: List<WordUI>)
    fun getListForCourse(): List<WordUI>
}

class TestDataHelper : ITestDataHelper {

    private val _listOfWords: MutableList<WordUI> = mutableListOf()
    private val _listForCourse: MutableList<WordUI> = _listOfWords

    override fun getListOfWords() = _listOfWords.toList()

    val smallList = listOf(
        WordUI("deutsches Wort", "немецкое слово", Level.getRandom()),
        WordUI("Katze", "Котик", Level.getRandom()),
        WordUI("Mutter", "Мама", Level.getRandom()),
        WordUI("Vater", "Отец, папа", Level.getRandom()),
        WordUI("Spiegel", "Зеркало", Level.getRandom()),
        WordUI("Sprache", "Язык, речь", Level.getRandom()),
        WordUI("deutsches Wort", "немецкое слово", Level.getRandom()),
        WordUI("Katze", "Котик", Level.getRandom()),
        WordUI("Mutter", "Мама", Level.getRandom())
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

    init {
        initList()
    }

    override fun updateList(newList: List<WordUI>) {
        _listForCourse.clear()
        _listForCourse.addAll(newList)
    }

    override fun getListForCourse(): List<WordUI> {
        return _listForCourse.toList()
    }

    private fun initList() {
        _listOfWords.clear()
        _listOfWords.addAll(smallList)
    }
}

val listOfDummyCards = listOf(
    WordUI("Text", "Текст", Level.NEW),
    WordUI("First word", "Первое слово", Level.NEW),
    WordUI("Second word", "Второе слово", Level.NEW),
    WordUI("Too loong word for translate", "Длинный текст для перевода", Level.NEW),
    WordUI("And the other one", "И еще одно слово", Level.NEW)
)
