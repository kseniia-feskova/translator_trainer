package com.presentation.ui.screens.lesson

import com.presentation.model.WordUI

data class LessonUIState(
    val preLoading: Boolean = false,
    val showStart: Boolean = false,
    val lessonData: Lesson? = null,
    val title: String = "",
    val description: String = "",
    val index: Int = 0,
    val complete: Boolean = false
)

sealed class Lesson(
    val options: List<String>,
    val selectedOption: String?,
    val correctOption: String,
    val currentWord: String
) {
    data class TranslateLesson(
        val words: Set<WordUI>,
        val selectedWord: WordUI?,
        val correctWord: WordUI
    ) : Lesson(
        options = words.map { it.resText },
        selectedOption = selectedWord?.resText,
        correctOption = correctWord.resText,
        currentWord = correctWord.originalText
    )

    data class OriginLesson(
        val words: Set<WordUI>,
        val selectedWord: WordUI?,
        val correctWord: WordUI
    ) : Lesson(
        options = words.map { it.originalText },
        selectedOption = selectedWord?.originalText,
        correctOption = correctWord.originalText,
        currentWord = correctWord.resText
    )

}

sealed class LessonIntent {
    object Start : LessonIntent()
    data class OptionSelected(val selected: String?) : LessonIntent()
    object DontKnow : LessonIntent()
}

enum class LessonType() {
    TRANSLATE,
    ORIGIN;

    fun mapToUI(): LessonUIState {
        return when (this) {
            TRANSLATE -> {
                LessonUIState(
                    title = "Задание №1",
                    description = "Выберите перевод",
                )
            }

            ORIGIN -> {
                LessonUIState(
                    title = "Задание №2",
                    description = "Выберите перевод",
                )
            }
        }
    }
}