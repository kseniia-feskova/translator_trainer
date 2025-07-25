package domain.mock

import data.model.course.CourseEntity
import data.translate.Language

val dummyCourses = listOf(
    CourseEntity(
        sourceLanguage = Language.RUSSIAN.name,
        targetLanguage = Language.GERMAN.name,
        id = "1",
        allWordsId = null,
        selectedSetId = null
    ),
    CourseEntity(
        sourceLanguage = Language.FRENCH.name,
        targetLanguage = Language.GERMAN.name,
        id = "2",
        allWordsId = null,
        selectedSetId = null
    ),
    CourseEntity(
        sourceLanguage = Language.RUSSIAN.name,
        targetLanguage = Language.FRENCH.name,
        id = "3",
        allWordsId = null,
        selectedSetId = null
    )
)