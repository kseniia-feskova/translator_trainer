package data.model.course

import kotlinx.serialization.Serializable

@Serializable
data class CourseEntity(
    @Serializable val id: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    @Serializable val allWordsId: String? = null,
    @Serializable val selectedSetId: String? = null,
)