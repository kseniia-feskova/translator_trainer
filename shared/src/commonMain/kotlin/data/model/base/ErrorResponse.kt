package data.model.base

data class ErrorResponse(
    val status: String,
    val details: ErrorDetails
)