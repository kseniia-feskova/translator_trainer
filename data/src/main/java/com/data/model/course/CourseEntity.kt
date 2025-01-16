package com.data.model.course

import com.data.model.UserEntity
import java.util.UUID

data class CourseEntity(
    var id: UUID,
    val sourceLanguage: String,
    val targetLanguage: String,
    val user_id: UserEntity,
    var all_words_id: UUID? = null,
    var selected_set_id: UUID? = null,
)