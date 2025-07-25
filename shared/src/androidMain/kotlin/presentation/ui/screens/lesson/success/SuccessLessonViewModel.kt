package presentation.ui.screens.lesson.success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import presentation.model.LessonType

class SuccessLessonViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val type: LessonType = savedStateHandle.type
    val wordsCount: Int = savedStateHandle.wordsCount

}