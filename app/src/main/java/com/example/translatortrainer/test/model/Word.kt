package com.example.translatortrainer.test.model

sealed class Word(open val level: Level) {

    data class WordUI(
        val originalText: String,
        val resText: String,
        override val level: Level
    ) : Word(level)


    data class WordUIFromRes(
        val textRes: Int,
        override val level: Level
    ) : Word(level)
}