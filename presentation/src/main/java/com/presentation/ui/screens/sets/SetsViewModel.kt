package com.presentation.ui.screens.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.usecases.IGetAllSetsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetsViewModel(
    private val getAllSets: IGetAllSetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetsUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val allSets = getAllSets.invoke().filter { it.setOfWords.isNotEmpty() }
            _uiState.update { it.copy(sets = allSets) }
        }
    }
}