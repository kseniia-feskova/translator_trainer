package data.model.course.add

import kotlinx.serialization.Serializable

@Serializable
data class AddCourseRequest(
    val name: String,
    val userId: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val allWordsId: String? = null,
    val selectedSetId: String? = null,
)
