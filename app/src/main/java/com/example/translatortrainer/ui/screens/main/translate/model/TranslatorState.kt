package com.example.translatortrainer.ui.screens.main.translate.model


data class TranslatorState(
    val inputText: String = "",
    val translatedText: String = "",
    val showGlow: Boolean = false
)

sealed class TranslatorIntent {
    data class InputingText(val text: String) : TranslatorIntent()
    data class EnterText(val text: String) : TranslatorIntent()
    object HideGlow : TranslatorIntent()
}