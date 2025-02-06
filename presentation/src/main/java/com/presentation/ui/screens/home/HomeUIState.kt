package com.presentation.ui.screens.home

import androidx.annotation.StringRes
import com.presentation.R
import com.presentation.utils.Language

data class HomeUIState(
    val loading: Boolean = false,
    val inputText: String = "",
    val translatedText: String = "",
    val showGlow: Boolean = false,
    val originalLanguage: Language = Language.RUSSIAN,
    val resLanguage: Language = Language.GERMAN,
    val isWordSaved: Boolean = false,
    val error: HomeError? = null
)

sealed class HomeIntent {
    data class InputText(val text: String) : HomeIntent()
    object EnterText : HomeIntent()
    object SaveWord : HomeIntent()
    data class ChangeLanguages(val selectedLang: Language) : HomeIntent()
}

enum class HomeError(@StringRes val msg: Int) {
    INTERNET_CONNECTION_ERROR(R.string.internet_connection_error),
    DEFAULT(R.string.default_error)
}
