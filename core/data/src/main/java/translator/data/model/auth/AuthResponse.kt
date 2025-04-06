package translator.data.model.auth

import java.util.UUID

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val uuid: UUID?,
    val error: String = ""
)