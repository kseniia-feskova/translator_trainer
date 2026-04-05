package com.translator.app.ui.screens.newset

import androidx.annotation.StringRes
import com.example.translatortrainer.shared.R
import presentation.model.WordUI

data class NewSetUIState(
    val loading: Boolean = false,
    val name: String = "",
    val isSaveChecked: Boolean = false,
    val query: String = "",
    val words: Map<WordUI, Boolean> = mapOf(),
    val countOfSelected: Int = 0,
    val error: NewSetError? = null,
    val limitsError: Boolean = false
)

enum class NewSetError(@StringRes val msg: Int) {
    EMPTY_FIELDS(R.string.empty_fields_error),
    EMPTY_SELECTION(R.string.empty_words_error),
    TAKEN_SET_NAME(R.string.set_name_taken_error),
    INTERNET_CONNECTION_ERROR(R.string.internet_connection_error),
    DEFAULT(R.string.default_error)
}
