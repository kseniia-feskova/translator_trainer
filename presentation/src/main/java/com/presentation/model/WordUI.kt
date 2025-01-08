package com.presentation.model

import java.util.Date
import java.util.UUID

// ui отображение WordEntity. Возможно понадобиться еще сохранять дату создания
data class WordUI(
    val id: UUID,
    val originalText: String,
    val resText: String,
    val level: Level,
    val date: Date = Date()
)


