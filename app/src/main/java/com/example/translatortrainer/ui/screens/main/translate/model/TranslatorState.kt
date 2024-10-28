package com.example.translatortrainer.ui.screens.main.translate.model


data class TranslatorState(
    val inputText: String = "",
    val translatedText: String = ""
)

sealed class TranslatorIntent {
    data class InputingText(val text: String) : TranslatorIntent()
    data class EnterText(val text: String) : TranslatorIntent()
}