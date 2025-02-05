package com.presentation.ui.screens.home

import com.presentation.utils.Language

data class HomeUIState(
    val loading: Boolean = false,
    val inputText: String = "",
    val translatedText: String = "",
    val showGlow: Boolean = false,
    val originalLanguage: Language = Language.RUSSIAN,
    val resLanguage: Language = Language.GERMAN,
    val isWordSaved: Boolean = false
)

sealed class HomeIntent {
    data class InputText(val text: String) : HomeIntent()
    object EnterText : HomeIntent()
    object SaveWord : HomeIntent()
    data class ChangeLanguages(val selectedLang: Language) : HomeIntent()
}