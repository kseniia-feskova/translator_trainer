package com.presentation.animation.stars

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.ui.AppTheme
import com.presentation.utils.toPx

val bgBrush = Brush.linearGradient(
    0.0f to Color.Black,
    0.7f to Color(0xFF140430),
    1f to Color(0xFF271460),
)

@Composable
fun SpaceFromOneDot() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    // Конвертация в пиксели с учетом плотности экрана
    val density = LocalDensity.current
    val screenWidthPx = with(density) { screenWidth.dp.toPx() }
    val screenHeightPx = with(density) { screenHeight.dp.toPx() }
    val radius = 40.dp.toPx()
    var points by remember {
        mutableStateOf(
            List(350) {
                Star(
                    x = screenWidthPx / 2,
                    y = screenHeightPx / 2,
                    angle = (0..360).random().toFloat(),
                    speed = (20..400).random().toFloat(),
                    size = (1 until 8).random(),
                    radius = radius,
                    alpha = ((0..10).random() / 10f)
                )
            }
        )
    }
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
                points = points.map { point ->
                    point.updatePosition(screenWidthPx, screenHeightPx, deltaTime, bounced = false)
                }.filterNotNull()
            }
        }
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
    ) {
        points.forEach { star ->
            drawCircle(
                color = Color(0xFFE2D5F8).copy(alpha = 0.8f),
                center = Offset(star.x, star.y),
                radius = star.size.dp.toPx(),
                blendMode = BlendMode.Screen
            )

        }
    }
}

@Composable
fun SpaceView() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    // Конвертация в пиксели с учетом плотности экрана
    val density = LocalDensity.current
    val screenWidthPx = with(density) { screenWidth.dp.toPx() }
    val screenHeightPx = with(density) { screenHeight.dp.toPx() }
    val radius = 40.dp.toPx()

    var points by remember {
        mutableStateOf(
            List(750) {
                Star(
                    x = (0 until screenWidthPx.toInt()).random().toFloat(),
                    y = (0 until screenHeightPx.toInt()).random().toFloat(),
                    angle = (0..360).random().toFloat(),
                    speed = (20..100).random().toFloat(),
                    size = (1 until 4).random(),
                    radius = radius,
                    alpha = 0.8f
                )
            }
        )
    }

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
                points = points.map { point ->
                    point.updatePosition(screenWidthPx, screenHeightPx, deltaTime)
                }.filterNotNull()
            }
        }
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
    ) {
        val maxConnections = 3

        // Карта для подсчёта связей
        val connections = mutableMapOf<Star, Int>()
        points.forEach { point ->
            connections[point] = 0
        }
        // Рисуем линии между точками, если они находятся в пределах радиуса
        for (i in points.indices) {
            for (j in i + 1 until points.size) {
                val pointA = points[i]
                val pointB = points[j]

                if (pointA.isWithinRadius(pointB) && pointA.size > 1 && pointB.size > 1) {
                    val connectionsA = connections[pointA] ?: 0
                    val connectionsB = connections[pointB] ?: 0

                    if (connectionsA < maxConnections && connectionsB < maxConnections) {
                        drawLine(
                            color = Color(0xFFF7F2FF),
                            start = Offset(pointA.x, pointA.y),
                            end = Offset(pointB.x, pointB.y),
                            strokeWidth = 2f
                        )

                        // Увеличиваем счётчик связей для обеих точек
                        connections[pointA] = connectionsA + 1
                        connections[pointB] = connectionsB + 1
                    }
                }
            }
        }
        points.forEach { star ->
            drawCircle(
                color = Color(0xFFE2D5F8).copy(alpha = star.alpha),
                center = Offset(star.x, star.y),
                radius = star.size.dp.toPx(),
                blendMode = BlendMode.Screen
            )

        }
    }
}


@Preview
@Composable
fun StarPreview() {
    AppTheme {
        Surface {
            SpaceView()
        }
    }
}

@Preview
@Composable
fun SpaceFromOneDotPreview() {
    AppTheme {
        Surface {
            SpaceFromOneDot()
        }
    }
}