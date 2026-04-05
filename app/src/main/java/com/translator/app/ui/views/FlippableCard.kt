package com.presentation.ui.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.translator.app.ui.AppTheme
import com.translator.app.ui.darkColor
import presentation.model.WordUI
import presentation.test.smallList
import com.translator.app.ui.screens.all.StarsRow

@Composable
fun FlippableCard(
    word: WordUI,
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
            .background(
                color = Color.White,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
        ) {
            if (!rotated) {
                Text(
                    text = word.originalText,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .graphicsLayer {
                            alpha = animateFront
                        },
                    style = MaterialTheme.typography.displayLarge,
                    color = darkColor,
                    textAlign = TextAlign.Center
                )
                StarsRow(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .graphicsLayer {
                            alpha = animateFront
                        },
                    stars = word.level.convertToStars()
                )
            } else {
                StarsRow(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                        .graphicsLayer {
                            rotationX = 180f  // Переворачиваем текст на обратной стороне
                            alpha = animateBack
                        },
                    stars = word.level.convertToStars()
                )
                Text(
                    text = word.resText,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .graphicsLayer {
                            rotationX = 180f  // Переворачиваем текст на обратной стороне
                            alpha = animateBack
                        },
                    style = MaterialTheme.typography.displayLarge,
                    color = darkColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}

@Composable
@Preview
fun FlippableCardPreview() {
    AppTheme {
        Surface {
            FlippableCard(smallList.first())
        }
    }
}