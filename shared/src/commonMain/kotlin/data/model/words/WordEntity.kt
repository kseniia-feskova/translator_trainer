package data.model.words

import kotlinx.serialization.Serializable

//TODO: get rid of and replace with WordResponse
@Serializable
data class WordEntity(
    val id: String,
    val originalText: String,
    val translatedText: String,
    val status: WordStatus
)