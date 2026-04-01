package data.model.sets

import kotlinx.serialization.Serializable

@Serializable
data class AddSetRequest(
    val name: String,
    val isDefault: Boolean = false,
    val courseId: String,
    val listOfWords: List<String> = emptyList()
)