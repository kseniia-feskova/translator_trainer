package com.domain.mapper

import com.data.model.WordStatus
import com.presentation.model.Level

fun Level.toStatus(): WordStatus {
    return when (this) {
        com.presentation.model.Level.NEW -> WordStatus.NEW
        com.presentation.model.Level.LEARNING_GOOD -> WordStatus.IN_GOOD_PROGRESS
        com.presentation.model.Level.LEARNING -> WordStatus.IN_PROGRESS
        com.presentation.model.Level.KNOW -> WordStatus.LEARNED
    }
}

fun WordStatus.toLevel(): Level {
    return when (this) {
        WordStatus.NEW -> Level.NEW
        WordStatus.IN_GOOD_PROGRESS -> Level.LEARNING_GOOD
        WordStatus.IN_PROGRESS -> Level.LEARNING
        WordStatus.LEARNED -> Level.KNOW
    }
}