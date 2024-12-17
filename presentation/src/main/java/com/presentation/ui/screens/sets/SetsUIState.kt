package com.presentation.ui.screens.sets

import com.presentation.model.SetOfCards

data class SetsUIState(
    val sets: List<SetOfCards> = emptyList(),
    val selectedSetId: Int? = null
)
