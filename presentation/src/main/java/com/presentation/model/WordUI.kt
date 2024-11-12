package com.presentation.model

import java.util.Date

// ui отображение WordEntity. Возможно понадобиться еще сохранять дату создания
data class WordUI(
    val id: Int,
    val originalText: String,
    val resText: String,
    val level: Level,
    val date: Date = Date()
)


