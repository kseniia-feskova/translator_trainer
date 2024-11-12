package com.presentation.ui.screens.main.translate.model

import com.presentation.utils.Language


data class TranslatorState(
    val inputText: String = "",
    val translatedText: String = "",
    val showGlow: Boolean = false,
    val originalLanguage: Language = Language.GERMAN,
    val resLanguage: Language = Language.RUSSIAN
)

sealed class TranslatorIntent {
    data class InputingText(val text: String) : TranslatorIntent()
    data class EnterText(
        val text: String,
        val originalLanguage: Language,
        val resLanguage: Language
    ) : TranslatorIntent()

    object HideGlow : TranslatorIntent()
    object ChangeLanguages : TranslatorIntent()
    object SaveWord : TranslatorIntent()
}