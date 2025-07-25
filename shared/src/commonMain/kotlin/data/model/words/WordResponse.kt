package data.model.words

import data.model.course.CourseEntity

data class WordResponse(
    val id: String,
    val originalText: String,
    val translatedText: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val course: CourseEntity,
    val version: Long = 0,
    val status: WordStatus = WordStatus.New
)
