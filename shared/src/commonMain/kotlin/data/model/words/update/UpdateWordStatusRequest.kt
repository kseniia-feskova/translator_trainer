package data.model.words.update

import data.model.words.WordStatus
import kotlinx.serialization.Serializable

@Serializable
data class UpdateWordStatusRequest(
    val status: WordStatus
)
