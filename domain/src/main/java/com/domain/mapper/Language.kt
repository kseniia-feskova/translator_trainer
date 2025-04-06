package com.domain.mapper

import com.presentation.utils.Language

fun Language.toData(): translator.data.translate.Language {
    return when (this) {
        Language.GERMAN -> translator.data.translate.Language.GERMAN
        else -> translator.data.translate.Language.RUSSIAN
    }
}