package com.presentation.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import presentation.ui.AppTypography
import com.presentation.ui.bgColor
import com.presentation.ui.lightLilaColor
import com.presentation.ui.whiteColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectableChipSet(
    allWords: List<String> = emptyList(),
    selectedWords: List<String> = emptyList(),
    onSelect: (String) -> Unit = {}
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
    ) {
        allWords.forEach { word ->
            val isSelected = word in selectedWords

            Chip(
                text = word,
                isSelected = isSelected,
                onClick = { onSelect(word) }
            )
        }
    }
}

@Composable
fun Chip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) bgColor else lightLilaColor,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Text(
            text = text,
            style = AppTypography.titleSmall.copy(fontSize = TextUnit(16f, TextUnitType.Sp)),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = if (isSelected) whiteColor else bgColor
        )
    }
}

@Preview
@Composable
fun SelectableChipSetPreview() {
    SelectableChipSet(listOf("Big", "text", "with", "a lot of", "words"))
}