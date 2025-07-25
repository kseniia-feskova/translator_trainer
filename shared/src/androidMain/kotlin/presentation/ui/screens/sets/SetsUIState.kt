package presentation.ui.screens.sets

import androidx.annotation.StringRes
import com.example.translatortrainer.shared.R
import presentation.model.SetOfCards

data class SetsUIState(
    val sets: List<SetOfCards> = emptyList(),
    val allWordsSet: String? = null,
    val selectedSetId: String? = null,
    val loading: Boolean = false,
    val error: SetsError? = null
)

enum class SetsError(@StringRes val msg: Int) {
    INTERNET_CONNECTION_ERROR(R.string.internet_connection_error),
    DEFAULT(R.string.default_error)
}