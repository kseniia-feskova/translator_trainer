package com.example.translatortrainer.test.model

import kotlin.random.Random

enum class Level {
    NEW,
    LEARNING,
    KNOW;

    companion object {
        fun getRandom(): Level {
            val enumValues = Level.values()
            return enumValues[Random.nextInt(enumValues.size)]
        }
    }
}