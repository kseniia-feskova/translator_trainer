package translator.data.model.translate

data class TranslateRequest(
    val contents: List<String>,
    val targetLanguageCode: String,
    val sourceLanguageCode: String? = null, // Можно оставить null для автоопределения
    val mimeType: String = "text/plain"
)
