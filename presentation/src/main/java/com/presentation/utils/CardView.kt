package com.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.presentation.ui.accentColor50
import com.presentation.ui.accentColor80
import com.presentation.ui.primaryColor
import com.presentation.ui.views.FlippableCard

@Composable
fun CardView(
    modifier: Modifier = Modifier,
    cardHeight: Dp = 250.dp,
    firstWordUIPreview: String,
    secondWordUIPreview: String?,
) {
    Box(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
    ) {

        if (secondWordUIPreview != null) {
            Box(
                modifier = Modifier
                    .height(cardHeight)
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .background(color = accentColor50, shape = RoundedCornerShape(24.dp))
                    .zIndex(2f),
            ) {
                Text(
                    text = secondWordUIPreview,
                    modifier = Modifier
                        .graphicsLayer {
                            renderEffect = BlurEffect(radiusX = 15f, radiusY = 15f)
                        }
                        .align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge,
                    color = primaryColor,
                    fontSize = TextUnit(22f, TextUnitType.Sp)
                )
            }
            Box(
                modifier = Modifier
                    .height(cardHeight)
                    .zIndex(1f)
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .background(color = accentColor80, shape = RoundedCornerShape(24.dp))
            ) {}
        }

        FlippableCard(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(cardHeight)
                .fillMaxWidth()
                .zIndex(3f)
                .align(Alignment.BottomCenter),
            frontText = firstWordUIPreview,
            backText = firstWordUIPreview,
            flipEnabled = false
        )

    }
}