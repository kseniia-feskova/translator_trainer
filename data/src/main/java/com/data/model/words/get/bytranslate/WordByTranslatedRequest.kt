package com.data.model.words.get.bytranslate

import java.util.UUID

data class WordByTranslatedRequest(
    val courseId: UUID,
    val translate: String
)