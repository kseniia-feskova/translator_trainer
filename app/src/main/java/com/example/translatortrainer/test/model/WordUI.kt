package com.example.translatortrainer.test.model

import com.data.model.WordEntity

data class WordUI(
    val id: Int,
    val originalText: String,
    val resText: String,
    val level: Level,
)

fun WordUI.toWordEntity(): WordEntity {
    return WordEntity(
        id = id,
        original = originalText,
        translation = resText,
        status = level.toStatus()
    )
}

