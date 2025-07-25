package presentation.model

import presentation.utils.Language
import kotlinx.serialization.Serializable

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