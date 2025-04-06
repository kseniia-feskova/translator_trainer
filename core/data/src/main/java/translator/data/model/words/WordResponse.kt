package translator.data.model.words

import translator.data.model.WordStatus
import translator.data.model.course.CourseEntity
import java.util.UUID

data class WordResponse(
    val id: UUID,
    val originalText: String,
    val translatedText: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val course: CourseEntity,
    val version: Long = 0,
    val status: WordStatus = WordStatus.New
)
