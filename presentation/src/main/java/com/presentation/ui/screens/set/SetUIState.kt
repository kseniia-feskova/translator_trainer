package com.presentation.ui.screens.set

import com.presentation.model.WordUI

data class SetUIState(
    val knowWords: Int = 0,
    val allWords: Int = 0,
    val name: String = "",
    val words: Pair<WordUI, WordUI?>? = null
)

sealed class CardSetIntent {
    data class AddWordToKnow(val word: WordUI) : CardSetIntent()
    data class AddWordToLearn(val word: WordUI) : CardSetIntent()
    object ResetCardSet : CardSetIntent()
}