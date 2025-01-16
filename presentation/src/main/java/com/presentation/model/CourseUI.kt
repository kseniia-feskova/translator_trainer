package com.presentation.model

import com.presentation.utils.Language
import java.util.UUID

data class CourseUI(
    val id: UUID,
    val originalLanguage: Language,
    val translateLanguage: Language,
    val allWordsId: UUID?,
    val selectedSetId: UUID?,
    val user: UserUI
)