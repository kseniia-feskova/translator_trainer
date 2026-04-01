package data.model.words.get.bytranslate

import kotlinx.serialization.Serializable

@Serializable
data class WordByTranslatedRequest(
    val courseId: String,
    val translate: String
)