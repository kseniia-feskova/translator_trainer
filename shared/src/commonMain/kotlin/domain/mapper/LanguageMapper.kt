package domain.mapper

import com.google.mlkit.nl.translate.TranslateLanguage
import data.translate.Language

fun Language.toTranslatorModel(): String {
    return when (this) {
        Language.GERMAN -> TranslateLanguage.GERMAN
        Language.FRENCH -> TranslateLanguage.FRENCH
        Language.ENGLISH -> TranslateLanguage.ENGLISH
        else -> TranslateLanguage.RUSSIAN
    }
}