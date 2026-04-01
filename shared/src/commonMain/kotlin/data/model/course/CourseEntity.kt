package data.model.course

import kotlinx.serialization.Serializable

@Serializable
data class CourseEntity(
    val id: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val allWordsId: String? = null,
    val selectedSetId: String? = null,
)