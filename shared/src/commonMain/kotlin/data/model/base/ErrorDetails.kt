package data.model.base

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDetails(
    val message: String
)