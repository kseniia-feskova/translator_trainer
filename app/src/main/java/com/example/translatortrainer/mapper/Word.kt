package com.example.translatortrainer.mapper

import com.data.model.WordEntity
import com.data.model.WordStatus
import com.example.translatortrainer.test.model.Level
import com.example.translatortrainer.test.model.WordUI

fun WordEntity.toWordUI(): WordUI {
    return WordUI(
        id = id,
        originalText = original,
        resText = translation,
        level = status.toLevel()
    )
}

fun WordStatus.toLevel(): Level {
    return when (this) {
        WordStatus.NEW -> Level.NEW
        WordStatus.LEARNED -> Level.KNOW
        WordStatus.IN_PROGRESS, WordStatus.IN_GOOD_PROGRESS -> Level.LEARNING
    }
}