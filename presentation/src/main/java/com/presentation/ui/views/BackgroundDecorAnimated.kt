package com.presentation.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.model.MovingBgCircle
import com.presentation.ui.AppTheme
import com.presentation.ui.darkColor
import com.presentation.ui.lightLilaColor
import com.presentation.ui.whiteColor
import kotlin.math.roundToInt

@Composable
fun BackgroundDecorAnimated() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val sizes =
        listOf(
            screenWidth.dp.value * 0.25,
            screenWidth.dp.value * 0.15,
            screenWidth.dp.value * 0.09
        )
    val density = LocalDensity.current
    val screenWidthPx = with(density) { screenWidth.dp.toPx() }
    val screenHeightPx = with(density) { screenHeight.dp.toPx() }
    val darkCircle = MovingBgCircle(
        x = (0..screenWidthPx.roundToInt()).random().toFloat(),
        y = (0..screenHeightPx.roundToInt()).random().toFloat(),
        angle = (90..360).random().toFloat(),
        speed = (20..400).random().toFloat(),
        size = sizes[0].roundToInt(),
        color = darkColor
    )
    val lightCircle = MovingBgCircle(
        x = (0..screenWidthPx.roundToInt()).random().toFloat(),
        y = (0..screenHeightPx.roundToInt()).random().toFloat(),
        angle = (30..360).random().toFloat(),
        speed = (20..400).random().toFloat(),
        size = sizes[1].roundToInt(),
        color = lightLilaColor
    )
    val whiteCircle = MovingBgCircle(
        x = (0..screenWidthPx.roundToInt()).random().toFloat(),
        y = (0..(screenHeightPx / 2).roundToInt()).random().toFloat(),
        angle = (40..270).random().toFloat(),
        speed = (50..200).random().toFloat(),
        size = sizes[2].roundToInt(),
        color = whiteColor
    )
    var points by remember { mutableStateOf(listOf(darkCircle, lightCircle, whiteCircle)) }
    var lastFrameTimeNanos = 0L

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { frameTimeNanos ->
                if (lastFrameTimeNanos == 0L) {
                    lastFrameTimeNanos = frameTimeNanos
                    return@withFrameNanos
                }
                val deltaTime = (frameTimeNanos - lastFrameTimeNanos) / 1_000_000_000f
                lastFrameTimeNanos = frameTimeNanos
                points = points.mapNotNull { point ->
                    point.updatePosition(screenWidthPx, screenHeightPx, deltaTime)
                }
            }
        }
    }
    Canvas(modifier = Modifier.fillMaxSize()) {
        points.forEach { star ->
            drawCircle(
                color = star.color,
                center = Offset(star.x, star.y),
                radius = star.size.dp.toPx(),
            )
        }
    }
}


@Preview
@Composable
fun BackgroundDecorAnimatedPreview() {
    AppTheme {
        Surface {
            BackgroundDecorAnimated()
        }
    }
}