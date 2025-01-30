package com.data.model.course

import java.util.UUID

data class CourseEntity(
    var id: UUID,
    val sourceLanguage: String,
    val targetLanguage: String,
    val userId: UUID,
    var allWordsId: UUID? = null,
    var selectedSetId: UUID? = null,
)