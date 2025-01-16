package com.presentation.ui.screens.sets

import com.presentation.model.SetOfCards
import java.util.UUID

data class SetsUIState(
    val sets: List<SetOfCards> = emptyList(),
    val selectedSetId: UUID? = null
)
