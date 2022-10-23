package com.example.translatortrainer.utils

import com.example.translatortrainer.data.WordEntity

object ConstTest {
    private val testList = mutableListOf<WordEntity>()
    private val word1 = WordEntity(0, "Katze", "Cat", "German", false)
    private val word2 = WordEntity(1, "Hund", "Dog", "German", false)

    fun getList(): List<WordEntity> {
        testList.add(word1)
        testList.add(word2)
        return testList
    }
}