package com.presentation.ui.screens.set

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.ui.accentColor
import com.presentation.ui.primaryColor
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun FlippableCard(
    frontText: String,
    backText: String,
    modifier: Modifier = Modifier,
    flipEnabled: Boolean = true
) {
    var rotated by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(800)
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!rotated) 1f else 0f,
        animationSpec = tween(500)
    )

    val animateBack by animateFloatAsState(
        targetValue = if (rotated) 1f else 0f,
        animationSpec = tween(500)
    )

    Box(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .graphicsLayer {
                rotationX = rotation
                cameraDistance = 8 * density
            }
            .clickable(indication = null, interactionSource = null)
            {
                if (flipEnabled) {
                    rotated = !rotated
                }
            }
            .height(300.dp)
            .background(color = accentColor, shape = RoundedCornerShape(24.dp))
    ) {
        if (!rotated) {
            Text(
                text = frontText,
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        alpha = animateFront
                    },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = primaryColor,
                fontSize = TextUnit(30f, TextUnitType.Sp)
            )
        } else {
            Text(
                text = backText,
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        rotationX = 180f  // Переворачиваем текст на обратной стороне
                        alpha = animateBack
                    },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = primaryColor,
                fontSize = TextUnit(30f, TextUnitType.Sp)
            )
        }
    }

}

@Composable
@Preview
fun FlippableCardPreview() {
    FlippableCard("Deutsche", "Немецкий")
}


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