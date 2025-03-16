package com.data

import com.data.model.WordEntity
import com.data.model.course.CourseEntity
import com.data.model.words.WordResponse

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
