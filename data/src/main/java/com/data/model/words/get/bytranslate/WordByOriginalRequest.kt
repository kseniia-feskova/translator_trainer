package com.data.model.words.get.bytranslate

import java.util.UUID

data class WordByOriginalRequest(
    val courseId: UUID,
    val original: String
)