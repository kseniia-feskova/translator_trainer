package com.domain.mapper

import com.data.model.course.CourseEntity
import com.presentation.model.CourseUI
import com.presentation.utils.getLanguageByCode

fun CourseEntity.toUI(): CourseUI {
    return CourseUI(
        id = id,
        selectedSetId = selected_set_id,
        allWordsId = all_words_id,
        translateLanguage = targetLanguage.getLanguageByCode(),
        originalLanguage = sourceLanguage.getLanguageByCode(),
        user = user_id.toUI()
    )
}