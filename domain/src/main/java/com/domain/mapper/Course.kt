package com.domain.mapper

import translator.data.model.course.CourseEntity
import com.presentation.model.CourseUI
import com.presentation.utils.getLanguageByCode
import com.presentation.utils.getResourceByCode
import java.util.UUID

fun String?.toUUID(): UUID? {
    if (this == null) return null
    return UUID.fromString(this)
}

fun CourseEntity.toUI(): CourseUI {
    return CourseUI(
        id = id.toString(),
        selectedSetId = selectedSetId?.toString(),
        allWordsId = allWordsId?.toString(),
        translateLanguage = targetLanguage.getLanguageByCode(),
        originalLanguage = sourceLanguage.getLanguageByCode(),
        originalFlag = sourceLanguage.getResourceByCode(),
        translatedFlag = targetLanguage.getResourceByCode(),
    )
}

fun CourseUI.toData(): CourseEntity {
    return CourseEntity(
        id = UUID.fromString(id),
        sourceLanguage = originalLanguage.code,
        targetLanguage = translateLanguage.code,
        allWordsId = allWordsId.toUUID(),
        selectedSetId = selectedSetId.toUUID()
    )
}