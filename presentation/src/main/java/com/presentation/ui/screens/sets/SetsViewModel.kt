package com.presentation.ui.screens.sets

import android.util.Log
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
            _uiState.update { it.copy(loading = true) }
            val course = prefs.getCourse()
            Log.e("SetsViewModel", "init, course = ${course?.id} ")
            if (course != null) {
                val response = getAllSets.invoke(UUID.fromString(course.id))
                if (response.isSuccess) {
                    val sets = response.getOrNull() ?: emptyList()
                    _uiState.update {
                        it.copy(
                            sets = if (sets.isNotEmpty() && sets[0].words.isEmpty()) emptyList() else sets,
                            loading = false
                        )
                    }
                } else {
                    handleError(response)
                }
            } else {
                _uiState.update { it.copy(loading = false) }
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

    private fun <T> handleError(response: Result<T>) {
        val error = response.exceptionOrNull()
        val errorMsg = when (error?.message) {
            "Failed to connect" -> SetsError.INTERNET_CONNECTION_ERROR
            else -> SetsError.DEFAULT
        }
        _uiState.update { it.copy(error = errorMsg, loading = false) }
    }
}