package com.example.translatortrainer.ui.screens.main.top

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.translatortrainer.ui.accentColor
import com.example.translatortrainer.ui.primaryColor

@Composable
fun DonutChart(
    percentage: Float,
    modifier: Modifier = Modifier,
    donutSize: Dp = 200.dp,
    strokeWidth: Float = 40f
) {
    // Ограничиваем процент значением от 0 до 100
    val clampedPercentage = percentage.coerceIn(0f, 100f)

    Box(
        modifier = modifier
            .size(donutSize)
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            // Размер холста
            val canvasSize = size.minDimension

            // Фон "пончика" (темный цвет)
            drawArc(
                color = accentColor,
                startAngle = 270f, // Начало заполнения с верхней точки
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(strokeWidth, strokeWidth),
                size = Size(canvasSize - 2 * strokeWidth, canvasSize - 2 * strokeWidth),
                style = Stroke(width = strokeWidth)
            )

            // Светлое заполнение (процентное значение)
            val sweepAngle = (clampedPercentage / 100) * 360
            drawArc(
                color = primaryColor,
                startAngle = 270f, // Начало заполнения с верхней точки
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(strokeWidth, strokeWidth),
                size = Size(canvasSize - 2 * strokeWidth, canvasSize - 2 * strokeWidth),
                style = Stroke(width = strokeWidth)
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 8.dp),
            text = "${percentage.toInt()}%",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = TextUnit(22f, TextUnitType.Sp),
            color = primaryColor,
        )
    }
}

@Preview
@Composable
fun DonutChartPreview() {
    DonutChart(percentage = 80f)
}
