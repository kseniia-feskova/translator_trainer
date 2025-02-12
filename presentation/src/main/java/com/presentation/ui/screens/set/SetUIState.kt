package com.presentation.ui.screens.set

import com.presentation.model.WordUI
import com.presentation.ui.screens.auth.BaseError

data class SetUIState(
    val knowWords: Int = 0,
    val allWords: Int = 0,
    val name: String = "",
    val words: Pair<WordUI, WordUI?>? = null,
    val loading: Boolean = false,
    val error: BaseError? = null,
    val selectLessonVisible: Boolean = false
)

sealed class CardSetIntent {
    data class AddWordToKnow(val word: WordUI) : CardSetIntent()
    data class AddWordToLearn(val word: WordUI) : CardSetIntent()
    data class CourseSelection(val isVisible: Boolean) : CardSetIntent()
    object ResetCardSet : CardSetIntent()
}