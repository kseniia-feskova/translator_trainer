package presentation.model

data class UserUI(
    val userId: String,
    val username: String,
    val email: String,
    val photo: String? = null,
    val courses: List<CourseUI> = emptyList()
)

sealed class UserResult {
    data class Existing(val user: UserUI) : UserResult()
    object New : UserResult()
    data class Error(val message: String) : UserResult()
}