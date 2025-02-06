package com.presentation.ui.screens.sets

import androidx.annotation.StringRes
import com.presentation.R
import com.presentation.model.SetOfCards
import java.util.UUID

data class SetsUIState(
    val sets: List<SetOfCards> = emptyList(),
    val selectedSetId: UUID? = null,
    val loading: Boolean = false,
    val error: SetsError? = null
)

enum class SetsError(@StringRes val msg: Int) {
    INTERNET_CONNECTION_ERROR(R.string.internet_connection_error),
    DEFAULT(R.string.default_error)
}