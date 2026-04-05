package com.presentation.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun CustomRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = Color.Black,
    unselectedColor: Color = Color.Gray
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                color = if (selected) selectedColor else unselectedColor,
                radius = size.minDimension / 2,
                style = Stroke(4f)
            )

            if (selected) {
                drawCircle(
                    color = selectedColor,
                    radius = size.minDimension / 6
                )
            }
        }
    }
}