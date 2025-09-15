package mapper

import presentation.utils.Language

fun Language.toData(): data.translate.Language {
    return when (this) {
        Language.GERMAN -> data.translate.Language.GERMAN
        Language.FRENCH -> data.translate.Language.FRENCH
        else -> data.translate.Language.RUSSIAN
    }
}