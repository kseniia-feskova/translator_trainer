package data

import data.model.course.CourseEntity
import data.model.words.WordResponse
import data.room.model.WordEntity

fun WordEntity.toWordResponse() = WordResponse(
    id = id,
    originalText = originalText,
    translatedText = translatedText,
    sourceLanguage = sourceLanguage,
    targetLanguage = targetLanguage,
    course = CourseEntity(
        id = courseId,
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
    ),
    status = status,
)
