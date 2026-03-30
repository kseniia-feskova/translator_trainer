package data.mock

import data.model.course.CourseEntity
import data.translate.Language

val dummyCourses = listOf(
    CourseEntity(
        sourceLanguage = Language.RUSSIAN.code,
        targetLanguage = Language.GERMAN.code,
        id = "1",
        allWordsId = null,
        selectedSetId = null
    ),
    CourseEntity(
        sourceLanguage = Language.FRENCH.code,
        targetLanguage = Language.GERMAN.code,
        id = "2",
        allWordsId = null,
        selectedSetId = null
    ),
    CourseEntity(
        sourceLanguage = Language.RUSSIAN.code,
        targetLanguage = Language.FRENCH.code,
        id = "3",
        allWordsId = null,
        selectedSetId = null
    ),
    CourseEntity(
        sourceLanguage = Language.RUSSIAN.code,
        targetLanguage = Language.ENGLISH.code,
        id = "4",
        allWordsId = null,
        selectedSetId = null
    ),
    CourseEntity(
        sourceLanguage = Language.ENGLISH.code,
        targetLanguage = Language.GERMAN.code,
        id = "5",
        allWordsId = null,
        selectedSetId = null
    )
)