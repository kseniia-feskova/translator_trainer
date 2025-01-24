package com.data.model.course

import com.data.model.UserEntity
import java.util.UUID

data class CourseEntity(
    var id: UUID,
    val sourceLanguage: String,
    val targetLanguage: String,
    val user: UserEntity,
    var allWordsId: UUID? = null,
    var selectedSetId: UUID? = null,
)