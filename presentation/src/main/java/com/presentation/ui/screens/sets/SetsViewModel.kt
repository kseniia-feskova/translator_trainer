package com.presentation.ui.screens.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import com.presentation.usecases.IGetAllSetsUseCase
import com.presentation.utils.ALL_WORDS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class SetsViewModel(
    private val getAllSets: IGetAllSetsUseCase,
    private val prefs: IDataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetsUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val courseId = prefs.getCourseId()
            if (courseId != null) {
                val response = getAllSets.invoke(courseId)
                if (response.isSuccess) {
                    _uiState.update { it.copy(sets = response.getOrNull() ?: emptyList()) }
                }
            }
        }
    }

    fun selectSet(setId: UUID?) {
        _uiState.update { it.copy(selectedSetId = setId) }
    }

    fun isAllWordsSelected(setId: UUID): Boolean {
        val selected = _uiState.value.sets.find { it.id == setId }
        return if (selected != null) {
            selected.title == ALL_WORDS
        } else false
    }
}