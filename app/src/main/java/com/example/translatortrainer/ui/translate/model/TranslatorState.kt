package com.example.translatortrainer.ui.translate.model


data class TranslatorState(
    val inputText: String = "",
    val translatedText: String = ""
)

sealed class TranslatorIntent {
    data class EnterText(val text: String) : TranslatorIntent()
}