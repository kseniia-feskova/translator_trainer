package presentation.model

data class FirebaseUser(
    val uuid: String? = null,
    val email: String? = null,
    val displayName: String? = null,
    val photo: String? = null,
    val phone: String? = null
)