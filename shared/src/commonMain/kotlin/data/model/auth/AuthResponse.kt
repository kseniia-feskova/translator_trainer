package data.model.auth

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val uuid: String?,
    val error: String = ""
)