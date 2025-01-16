package com.presentation.model

import com.presentation.R
import com.presentation.utils.Language

data class CoursePreview(
    val originalLanguage: Language,
    val translateLanguage: Language,
    val originalFlag: Int,
    val translatedFlag: Int
)

val ruDeCourse = CoursePreview(
    originalFlag = R.drawable.russia,
    translatedFlag = R.drawable.germany,
    originalLanguage = Language.RUSSIAN,
    translateLanguage = Language.GERMAN
)