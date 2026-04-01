package data.model.words.get.bytranslate

import kotlinx.serialization.Serializable

@Serializable
data class WordByOriginalRequest(
    val courseId: String,
    val original: String
)