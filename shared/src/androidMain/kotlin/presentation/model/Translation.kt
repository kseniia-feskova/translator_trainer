package presentation.model

data class Translation(
    val resource: String,
    val translating: String,
    val altTranslate: List<String>
)