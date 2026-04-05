package com.translator.app.ui.screens.set

import android.content.Context
import android.net.Uri
import presentation.model.WordUI
import com.translator.app.ui.screens.auth.BaseError

data class SetUIState(
    val knowWords: Int = 0,
    val allWords: Int = 0,
    val name: String = "",
    val words: Pair<WordUI, WordUI?>? = null,
    val loading: Boolean = false,
    val error: BaseError? = null,
    val selectLessonVisible: Boolean = false
)

sealed class SetUIEvent {
    data object RequestExport : SetUIEvent()
}

sealed class CardSetIntent {
    data class AddWordToKnow(val word: WordUI) : CardSetIntent()
    data class AddWordToLearn(val word: WordUI) : CardSetIntent()
    data class CourseSelection(val isVisible: Boolean) : CardSetIntent()
    object ResetCardSet : CardSetIntent()
    object DownloadCardSet : CardSetIntent()
    data class ExportFile(val uri: Uri, val context: Context) : CardSetIntent()
}