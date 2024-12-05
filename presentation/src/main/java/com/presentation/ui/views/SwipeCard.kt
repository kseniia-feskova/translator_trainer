package com.presentation.ui.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun SwipeCard(
    modifier: Modifier = Modifier,
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    swipeThreshold: Float = 400f,
    sensitivityFactor: Float = 3f,
    swipeEnabled: Boolean = true,
    content: @Composable () -> Unit
) {
    var offset by remember { mutableStateOf(0f) }
    var dismissRight by remember { mutableStateOf(false) }
    var dismissLeft by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density

    LaunchedEffect(dismissRight) {
        if (dismissRight) {
            delay(600)
            onSwipeRight.invoke()
            dismissRight = false
            offset = 0f
        }
    }

    LaunchedEffect(dismissLeft) {
        if (dismissLeft) {
            delay(600)
            onSwipeLeft.invoke()
            dismissLeft = false
            offset = 0f
        }
    }

    Box(
        modifier = modifier
            .offset { IntOffset(offset.roundToInt(), 0) }
            .pointerInput(Unit) {
                if (swipeEnabled) {
                    detectHorizontalDragGestures(onDragEnd = {
                        // Проверяем, прошла ли карточка порог
                        if (offset > swipeThreshold) {
                            dismissRight = true
                        } else if (offset < -swipeThreshold) {
                            dismissLeft = true
                        }

                        // Возвращаем карточку на место, если не прошли порог
                        if (!dismissRight && !dismissLeft) {
                            offset = 0f
                        } else {
                            // Двигаем карточку за пределы экрана
                            offset = if (dismissRight) 900f else -900f
                        }
                    }) { change, dragAmount ->
                        offset += (dragAmount / density) * sensitivityFactor

                        // Проверяем, прошла ли карточка порог
                        if (offset > swipeThreshold) {
                            dismissRight = true
                        } else if (offset < -swipeThreshold) {
                            dismissLeft = true
                        }

                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }
            }
            .graphicsLayer(

                alpha = 1f - animateFloatAsState(if (dismissRight || dismissLeft) 1f else 0f).value,
                rotationZ = animateFloatAsState(offset / 50).value
            )
    ) {
        content()
    }

}

@Preview
@Composable
fun SwipeCardPreview() {
    SwipeCard() {
        FlippableCard(frontText = "Deutsches Wort", backText = "Немецкое слово")
    }
}