package data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String,
    val phone: String? = "",
    val email: String? = "",
    val password: String
)