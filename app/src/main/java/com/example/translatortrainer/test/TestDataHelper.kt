package com.example.translatortrainer.test

import com.example.translatortrainer.R
import com.example.translatortrainer.test.model.Level
import com.example.translatortrainer.test.model.Word

interface ITestDataHelper {
    fun getListOfWords(): List<Word>

}

class TestDataHelper : ITestDataHelper {

    private val _listOfWords: MutableList<Word> = mutableListOf()

    override fun getListOfWords() = _listOfWords.toList()

    val smallList = listOf(
        Word.WordUI("deutsches Wort", "немецкое слово", Level.getRandom()),
        Word.WordUI("Katze", "Котик", Level.getRandom()),
        Word.WordUI("Mutter", "Мама", Level.getRandom()),
        Word.WordUI("Vater", "Отец, папа", Level.getRandom()),
        Word.WordUI("Spiegel", "Зеркало", Level.getRandom()),
        Word.WordUI("Sprache", "Язык, речь", Level.getRandom()),
        Word.WordUI("deutsches Wort", "немецкое слово", Level.getRandom()),
        Word.WordUI("Katze", "Котик", Level.getRandom()),
        Word.WordUI("Mutter", "Мама", Level.getRandom())
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

    private fun initList() {
        _listOfWords.clear()
        val newList = mutableListOf<Word>()
        listOfResource.map {
            newList.add(
                Word.WordUIFromRes(
                    it,
                    level = Level.getRandom()
                )
            )
        }
        _listOfWords.addAll(newList)
    }
}