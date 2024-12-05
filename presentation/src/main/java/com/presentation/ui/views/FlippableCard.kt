package com.presentation.ui.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.ui.AppTheme
import com.presentation.ui.primaryColor

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
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(24.dp)
            )
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
    AppTheme {
        Surface {
            FlippableCard("Deutsche", "Немецкий")
        }
    }
}