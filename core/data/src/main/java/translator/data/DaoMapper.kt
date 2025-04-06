package translator.data

import translator.data.model.WordEntity
import translator.data.model.course.CourseEntity
import translator.data.model.words.WordResponse

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
