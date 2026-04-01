package data.mock

import data.model.course.CourseEntity
import data.translate.Language
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
val dummyCourses = listOf(
    CourseEntity(
        sourceLanguage = Language.RUSSIAN.code,
        targetLanguage = Language.GERMAN.code,
        id = Uuid.random().toString(),
        allWordsId = null,
        selectedSetId = null
    ),
    CourseEntity(
        sourceLanguage = Language.FRENCH.code,
        targetLanguage = Language.GERMAN.code,
        id = Uuid.random().toString(),
        allWordsId = null,
        selectedSetId = null
    ),
    CourseEntity(
        sourceLanguage = Language.RUSSIAN.code,
        targetLanguage = Language.FRENCH.code,
        id = Uuid.random().toString(),
        allWordsId = null,
        selectedSetId = null
    ),
    CourseEntity(
        sourceLanguage = Language.RUSSIAN.code,
        targetLanguage = Language.ENGLISH.code,
        id = Uuid.random().toString(),
        allWordsId = null,
        selectedSetId = null
    ),
    CourseEntity(
        sourceLanguage = Language.ENGLISH.code,
        targetLanguage = Language.GERMAN.code,
        id = Uuid.random().toString(),
        allWordsId = null,
        selectedSetId = null
    )
)