package com.data.model.sets

import java.util.UUID

data class AddSetRequest(
    val name: String,
    val isDefault: Boolean = false,
    val courseId: UUID,
    val listOfWords: List<UUID> = emptyList()
)