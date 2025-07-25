package data.model.sets

data class AddSetRequest(
    val name: String,
    val isDefault: Boolean = false,
    val courseId: String,
    val listOfWords: List<String> = emptyList()
)