package com.domain.mapper

import com.data.model.WordStatus
import com.presentation.model.Level

fun Level.toStatus(): WordStatus {
    return when (this) {
        com.presentation.model.Level.NEW -> WordStatus.Known
        com.presentation.model.Level.LEARNING_GOOD -> WordStatus.GoodLearning
        com.presentation.model.Level.LEARNING -> WordStatus.Learning
        com.presentation.model.Level.KNOW -> WordStatus.Known
    }
}

fun WordStatus.toLevel(): Level {
    return when (this) {
        WordStatus.New -> Level.NEW
        WordStatus.GoodLearning -> Level.LEARNING_GOOD
        WordStatus.Learning -> Level.LEARNING
        WordStatus.Known -> Level.KNOW
    }
}