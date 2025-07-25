package data.model.course.add

data class AddCourseRequest(
    val name: String,
    val userId: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val allWordsId: String? = null,
    val selectedSetId: String? = null,
)
