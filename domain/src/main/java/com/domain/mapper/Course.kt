package com.domain.mapper

import com.data.model.course.CourseEntity
import com.presentation.model.CourseUI
import com.presentation.utils.getLanguageByCode

fun CourseEntity.toUI(): CourseUI {
    return CourseUI(
        id = id,
        selectedSetId = selectedSetId,
        allWordsId = allWordsId,
        translateLanguage = targetLanguage.getLanguageByCode(),
        originalLanguage = sourceLanguage.getLanguageByCode(),
        user = user.toUI()
    )
}