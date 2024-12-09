package com.presentation.model

import kotlin.random.Random

//description for word
// Word can be new, learning, learning good or known
enum class Level {
    NEW,
    LEARNING,
    LEARNING_GOOD,
    KNOW;

    companion object {
        fun getRandom(): Level {
            val enumValues = Level.values()
            return enumValues[Random.nextInt(enumValues.size)]
        }
    }

    fun convertToStars(): List<Boolean> {
        return when (this) {
            KNOW -> listOf(true, true, true)
            LEARNING_GOOD -> listOf(true, true, false)
            LEARNING -> listOf(true, false, false)
            else -> listOf(false, false, false)
        }
    }

    fun inc(): Level {
        return when (this) {
            KNOW -> KNOW
            LEARNING_GOOD -> KNOW
            LEARNING -> LEARNING_GOOD
            else -> LEARNING
        }
    }
}
