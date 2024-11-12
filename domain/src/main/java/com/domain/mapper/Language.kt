package com.domain.mapper

import com.presentation.utils.Language

fun Language.toData(): com.data.translate.Language {
    return when (this) {
        Language.GERMAN -> com.data.translate.Language.GERMAN
        else -> com.data.translate.Language.RUSSIAN
    }
}