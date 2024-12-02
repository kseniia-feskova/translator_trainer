package com.presentation.ui.screens.home

import com.presentation.utils.Language

data class HomeUIState(
    val inputText: String = "",
    val translatedText: String = "",
    val showGlow: Boolean = false,
    val originalLanguage: Language = Language.GERMAN,
    val resLanguage: Language = Language.RUSSIAN
)

sealed class HomeIntent {
    data class InputText(val text: String) : HomeIntent()
    data class EnterText(val text: String) : HomeIntent()
    object SaveWord : HomeIntent()
}