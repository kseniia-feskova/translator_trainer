package com.presentation.model

import com.presentation.utils.Language
import java.util.UUID

data class CourseUI(
    val id: UUID,
    val originalLanguage: Language,
    val translateLanguage: Language,
    val originalFlag: Int,
    val translatedFlag: Int,
    val allWordsId: UUID?,
    val selectedSetId: UUID?,
    val userId: UUID
)