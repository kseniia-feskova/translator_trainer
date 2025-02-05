package com.domain.mapper

import com.data.model.course.CourseEntity
import com.presentation.model.CourseUI
import com.presentation.utils.getLanguageByCode
import com.presentation.utils.getResourceByCode

fun CourseEntity.toUI(): CourseUI {
    return CourseUI(
        id = id.toString(),
        selectedSetId = selectedSetId?.toString(),
        allWordsId = allWordsId?.toString(),
        translateLanguage = targetLanguage.getLanguageByCode(),
        originalLanguage = sourceLanguage.getLanguageByCode(),
        originalFlag = sourceLanguage.getResourceByCode(),
        translatedFlag = targetLanguage.getResourceByCode(),
        userId = userId.toString()
    )
}