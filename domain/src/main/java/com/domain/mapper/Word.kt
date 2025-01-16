package com.domain.mapper

import com.data.model.WordEntity
import com.data.model.words.WordResponse
import com.presentation.model.Level
import com.presentation.model.WordUI
import java.util.UUID

fun WordUI.toNewWordEntity(): WordEntity {
    return WordEntity(
        original = originalText,
        translation = resText,
        status = level.toStatus(),
        dateAdded = date
    )
}
fun WordUI.toWordEntity(): WordEntity {
    return WordEntity(
        id = id,
        original = originalText,
        translation = resText,
        status = level.toStatus(),
        dateAdded = date
    )
}

fun WordEntity.toWord(): WordUI {
    return WordUI(
        id = id,
        originalText = original,
        resText = translation,
        level = status.toLevel(),
        date = dateAdded
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
        level = Level.NEW
    )
}