package com.translator.app.ui.screens.lesson.dictation.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import presentation.cache.IUiDataManager
import presentation.model.LessonType
import presentation.model.WordResult

class DictationResultViewModel(
    savedStateHandle: SavedStateHandle,
    private val uiDataManager: IUiDataManager
) : ViewModel() {

    val type: LessonType = savedStateHandle.dictationType
    val answers: List<WordResult>  = uiDataManager.getWordsTranslation()

    fun onContinue(){
        uiDataManager.saveWordsTranslation(emptyList())
    }
}