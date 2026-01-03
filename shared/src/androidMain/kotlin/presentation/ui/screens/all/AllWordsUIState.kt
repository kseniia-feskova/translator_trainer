package presentation.ui.screens.all

import presentation.model.WordUI
import presentation.model.WordViewData

data class AllWordsUIState(
    val loading: Boolean = false,
    val words: List<WordViewData> = emptyList(),
    val query: String = "",
    val filters: List<WordViewData> = emptyList()
)

sealed class AllWordsIntent {
    data class Search(val query: String) : AllWordsIntent()
    object ClearSearch : AllWordsIntent()
    object Filter : AllWordsIntent()
    data class Edit(val word: WordUI) : AllWordsIntent()
    data class Delete(val word: WordUI) : AllWordsIntent()

    data class OnActionsRevealed(val word: WordViewData) : AllWordsIntent()
    data class OnCollapsed(val word: WordViewData) : AllWordsIntent()
}