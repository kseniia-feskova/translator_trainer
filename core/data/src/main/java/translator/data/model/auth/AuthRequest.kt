package translator.data.model.auth

data class AuthRequest(
    val username: String,
    val phone: String? = "",
    val email: String? = "",
    val password: String
)