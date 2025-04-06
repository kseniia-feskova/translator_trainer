package com.domain.mapper

import translator.data.model.WordEntity
import translator.data.model.words.WordResponse
import com.presentation.model.Level
import com.presentation.model.WordUI
import java.util.UUID

fun WordEntity.toWord(): WordUI {
    return WordUI(
        id = id,
        originalText = originalText,
        resText = translatedText,
        level = status.toLevel(),
//        date = dateAdded
    )
}

fun WordResponse.toDao(): WordUI {
    return WordUI(
        id = UUID.randomUUID(),
        originalText = originalText,
        resText = translatedText,
        level = Level.NEW
    )
}


fun WordResponse.toUI(): WordUI {
    return WordUI(
        id = id,
        resText = translatedText,
        originalText = originalText,
        level = status.toLevel()
    )
}