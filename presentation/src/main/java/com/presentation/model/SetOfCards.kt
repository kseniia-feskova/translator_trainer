package com.presentation.model

import java.util.UUID

data class SetOfCards(
    val id: UUID,
    val title: String,
    val isDefault: Boolean,
    val words: List<WordUI>,
)
