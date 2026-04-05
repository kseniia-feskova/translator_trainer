package com.translator.app.ui.screens.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.usecases.sets.IGetAllSetsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import presentation.model.LessonType
import domain.usecases.sets.IGetAllWordsIdUseCase
import mapper.toUI

class SetsViewModel(
    getAllSets: IGetAllSetsUseCase,
    getAllWordsId: IGetAllWordsIdUseCase
) : ViewModel() {

    private val setsUiWithSetsFlow = getAllSets.invokeFlow()
        .map { sets -> SetsUIState(sets = sets.map { it.toUI() }, loading = false) }
        .catch { emit(SetsUIState(error = handleError(it))) }
        .onStart { emit(SetsUIState(loading = true)) }

    private val selectedSetId = MutableStateFlow<String?>(null)
    private val allWordsId = getAllWordsId.invoke()

    val uiState =
        combine(
            setsUiWithSetsFlow,
            selectedSetId,
            allWordsId
        ) { setsUIState, selected, allWordsId ->
            setsUIState.copy(
                selectedSetId = selected,
                allWordsSet = allWordsId
            )
        }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                SetsUIState(loading = true)
            )

    fun isAllWordsSelected(setId: String): Boolean {
        return uiState.value.allWordsSet == setId
    }

    private fun handleError(throwable: Throwable): SetsError {
        return when (throwable.message) {
            "Failed to connect" -> SetsError.INTERNET_CONNECTION_ERROR
            else -> SetsError.DEFAULT
        }
    }

    fun createRandomLesson(
        navigateToLesson: (String, LessonType) -> Unit = { _, _ -> },
    ) {
        val type = LessonType.entries.random()
        val setId = uiState.value.sets.shuffled()[0].id
        navigateToLesson(setId, type)
    }
}