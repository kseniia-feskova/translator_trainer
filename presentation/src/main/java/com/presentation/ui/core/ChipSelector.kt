package com.presentation.ui.core

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.ui.greenColor
import com.presentation.ui.lightGreenColor
import com.presentation.ui.primaryColor
import com.presentation.ui.redColor
import com.presentation.ui.secondaryColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipSelector(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedOption: String,
    correctAnswer: String,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.then(modifier)) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Выберите перевод", style = MaterialTheme.typography.bodyLarge,
            color = secondaryColor,
            fontSize = TextUnit(18f, TextUnitType.Sp)
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                val isCorrect = option == correctAnswer && isSelected
                val isIncorrect = option != correctAnswer && isSelected
                AnimatedChip(
                    text = option,
                    isSelected = isSelected,
                    isCorrect = isCorrect,
                    isIncorrect = isIncorrect,
                    onClick = {
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}

@Composable
fun AnimatedChip(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isIncorrect: Boolean,
    onClick: () -> Unit,
) {
    var showGlow by remember { mutableStateOf(false) }

    // Trigger glow effect on correct or incorrect selection
    LaunchedEffect(isCorrect, isIncorrect) {
        if (isCorrect || isIncorrect) {
            showGlow = true
            delay(1000) // Duration of the glow effect
            showGlow = false // Reset glow after delay
        }
    }

    // Animated color based on glow state and correctness
    val targetColor by animateColorAsState(
        targetValue = when {
            showGlow && isCorrect -> lightGreenColor
            showGlow && isIncorrect -> redColor
            else -> Color.Transparent
        },
        animationSpec = tween(durationMillis = 300)
    )

    // Animated color based on glow state and correctness
    val targetTextColor by animateColorAsState(
        targetValue = when {
            showGlow && isCorrect -> greenColor
            showGlow && isIncorrect -> redColor
            else -> primaryColor
        },
        animationSpec = tween(durationMillis = 300)
    )

    Surface(
        modifier = Modifier
            .wrapContentHeight()
            .shadow(
                elevation = 18.dp,
                clip = false,
                shape = RoundedCornerShape(24.dp),
                ambientColor = targetColor,
                spotColor = targetColor
            ),
        shape = RoundedCornerShape(18.dp),
        onClick = { onClick() }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = targetTextColor,
            maxLines = 2,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}


@Preview
@Composable
fun ChipSelectionDemo() {
    Box(modifier = Modifier.background(primaryColor)) {
        var selectedOption by remember { mutableStateOf("Option 1") }
        val options = listOf("Option", "Option 2", "Option 3", "Option 4", "Option 5", "Option 6")

        ChipSelector(
            options = options,
            selectedOption = selectedOption,
            correctAnswer = "Option 2",
            onOptionSelected = { selectedOption = it }
        )
    }
}