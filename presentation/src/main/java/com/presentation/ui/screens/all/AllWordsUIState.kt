package com.presentation.ui.screens.all

import androidx.compose.ui.geometry.Offset
import com.presentation.model.WordUI

data class AllWordsUIState(
    val loading: Boolean = false,
    val words: List<WordUI> = emptyList(),
    val query: String = "",
    val filters: List<String> = emptyList(),
    val selectedItem: WordUI? = null,
    val popupOffset: Offset? = null
)

sealed class AllWordsIntent() {
    data class Search(val query: String) : AllWordsIntent()
    object ClearSearch : AllWordsIntent()
    object Filter : AllWordsIntent()
    data class WordSelected(val selected: WordUI, val dialogOffset: Offset) : AllWordsIntent()
    object CloseDialog : AllWordsIntent()
    data class Edit(val word: WordUI) : AllWordsIntent()
    data class Delete(val word: WordUI) : AllWordsIntent()
}