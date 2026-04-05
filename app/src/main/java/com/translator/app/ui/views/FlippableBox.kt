package com.translator.app.ui.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun FlippableBox(
    modifier: Modifier = Modifier,
    isFrontVisible: Boolean = true,
    frontContent: @Composable BoxScope.(modifier: Modifier, onFlip: () -> Unit) -> Unit,
    backContent: @Composable BoxScope.(modifier: Modifier, onFlip: () -> Unit) -> Unit,
) {
    var flipped by remember { mutableStateOf(!isFrontVisible) } // Инвертируем начальное значение
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(800)
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!flipped) 1f else 0f,
        animationSpec = tween(500)
    )

    val animateBack by animateFloatAsState(
        targetValue = if (flipped) 1f else 0f,
        animationSpec = tween(500)
    )
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val cardSize = screenHeight * 0.5f
    Box(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
            .height(cardSize)
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    alpha = animateFront
                    rotationY = 0f
                }
        ) {
            frontContent(
                Modifier.align(Alignment.Center)
            ) {
                flipped = !flipped
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    alpha = animateBack
                    scaleX = -1f
                }
        ) {
            backContent(
                Modifier.align(Alignment.Center)
            ) {
                flipped = !flipped
            }
        }
    }
}