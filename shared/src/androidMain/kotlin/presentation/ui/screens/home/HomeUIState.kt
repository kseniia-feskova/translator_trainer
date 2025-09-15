package presentation.ui.screens.home

import androidx.annotation.StringRes
import com.example.translatortrainer.shared.R
import presentation.utils.Language

data class HomeUIState(
    val loading: Boolean = false,
    val inputText: String = "",
    val translatedText: String = "",
    val showGlow: Boolean = false,
    val originalLanguage: Language? = null,
    val resLanguage: Language? = null,
    val isWordSaved: Boolean = false,
    val error: HomeError? = null,
    val limitsError: Boolean = false
)

sealed class HomeIntent {
    data class InputText(val text: String) : HomeIntent()
    object EnterText : HomeIntent()
    object SaveWord : HomeIntent()
    data class ChangeLanguages(val selectedLang: Language) : HomeIntent()
    object HideLimitsError : HomeIntent()
}

enum class HomeError(@StringRes val msg: Int) {
    INTERNET_CONNECTION_ERROR(R.string.internet_connection_error),
    DEFAULT(R.string.default_error)
}
