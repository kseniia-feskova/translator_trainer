package mapper

import data.model.course.CourseEntity
import presentation.model.CourseUI
import presentation.getLanguageByCode
import presentation.getResourceByCode

fun CourseEntity.toUI(): CourseUI {
    return CourseUI(
        id = id,
        selectedSetId = selectedSetId,
        allWordsId = allWordsId,
        translateLanguage = targetLanguage.getLanguageByCode(),
        originalLanguage = sourceLanguage.getLanguageByCode(),
        originalFlag = sourceLanguage.getResourceByCode(),
        translatedFlag = targetLanguage.getResourceByCode(),
    )
}

fun CourseUI.toData(): CourseEntity {
    return CourseEntity(
        id = id,
        sourceLanguage = originalLanguage.code,
        targetLanguage = translateLanguage.code,
        allWordsId = allWordsId,
        selectedSetId = selectedSetId
    )
}