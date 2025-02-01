package com.data.model.course.add

import java.util.UUID

data class AddCourseRequest(
    val name: String,
    val userId: UUID,
    val sourceLanguage: String,
    val targetLanguage: String,
    val allWordsId: UUID? = null,
    val selectedSetId: UUID? = null,
)
