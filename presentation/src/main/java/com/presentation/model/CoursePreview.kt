package com.presentation.model

import com.presentation.R
import com.presentation.utils.Language
import java.util.UUID

val ruDeCourse = CourseUI(
    id = UUID.randomUUID(),
    originalFlag = R.drawable.russia,
    translatedFlag = R.drawable.germany,
    originalLanguage = Language.RUSSIAN,
    translateLanguage = Language.GERMAN,
    userId = UUID.randomUUID(),
    allWordsId = null,
    selectedSetId = null

)