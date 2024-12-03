package com.presentation.ui.screens.home

import com.presentation.utils.Language

data class HomeUIState(
    val loading:Boolean = false,
    val inputText: String = "",
    val translatedText: String = "",
    val showGlow: Boolean = false,
    val savedWord:Boolean = false,
    val originalLanguage: Language = Language.RUSSIAN,
    val resLanguage: Language = Language.GERMAN
)

sealed class HomeIntent {
    data class InputText(val text: String) : HomeIntent()
    data class EnterText(val text: String) : HomeIntent()
    object SaveWord : HomeIntent()
    object ChangeLanguages : HomeIntent()
}