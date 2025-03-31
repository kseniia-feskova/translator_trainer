package com.presentation.model

import androidx.compose.runtime.Stable
import com.presentation.utils.Language
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class CourseUI(
    val id: String,
    val originalLanguage: Language,
    val translateLanguage: Language,
    val originalFlag: Int,
    val translatedFlag: Int,
    val allWordsId: String?,
    val selectedSetId: String?,
)