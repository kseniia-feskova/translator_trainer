package com.example.translatortrainer.test.model

import com.data.model.WordStatus
import kotlin.random.Random

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
}

fun Level.toStatus(): WordStatus {
    return when (this) {
        Level.NEW -> WordStatus.NEW
        Level.LEARNING_GOOD -> WordStatus.IN_GOOD_PROGRESS
        Level.LEARNING -> WordStatus.IN_PROGRESS
        Level.KNOW -> WordStatus.LEARNED
    }
}