package data.model.base

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: String,
    val details: ErrorDetails
)