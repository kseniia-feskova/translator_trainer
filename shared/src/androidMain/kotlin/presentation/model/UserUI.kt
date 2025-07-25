package presentation.model

data class UserUI(
    val userId: String,
    val username: String,
    val email: String,
    val photo: String? = null,
    val courses: List<CourseUI> = emptyList()
)
