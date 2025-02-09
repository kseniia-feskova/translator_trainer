package com.domain.mapper

import com.data.model.WordEntity
import com.data.model.words.WordResponse
import com.presentation.model.Level
import com.presentation.model.WordUI
import java.util.UUID

fun WordUI.toNewWordEntity(): WordEntity {
    return WordEntity(
        originalText = originalText,
        translatedText = resText,
        status = level.toStatus(),
//        dateAdded = date
    )
}

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