package data.model.sets

import data.model.words.WordEntity

data class SetResponse(
    val id: String,
    val name: String,
    val isDefault: Boolean = false,
    val words: List<WordEntity> = emptyList()
)
