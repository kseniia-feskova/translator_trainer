package com.domain.mapper

import com.data.model.WordEntity
import com.presentation.model.WordUI

fun WordUI.toWordEntity(): WordEntity {
    return WordEntity(
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