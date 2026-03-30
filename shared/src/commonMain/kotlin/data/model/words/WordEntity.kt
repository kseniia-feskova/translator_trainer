package data.model.words

//TODO: get rid of and replace with WordResponse
data class WordEntity(
    val id: String,
    val originalText: String,
    val translatedText: String,
    val status: WordStatus,
    val sourceLanguage: String,
    val targetLanguage: String,
    val courseId: String
)