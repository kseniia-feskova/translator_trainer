package com.translator.app.ui.screens.lesson.match.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.translator.app.ui.AppTheme
import com.translator.app.ui.darkColor
import com.translator.app.ui.whiteColor
import com.translator.app.ui.screens.lesson.match.GridWithButtons
import com.translator.app.ui.screens.lesson.match.model.MatchViewData
import com.translator.app.ui.screens.lesson.match.model.testOriginalData
import com.translator.app.ui.screens.lesson.match.model.testTranslationData
import com.translator.app.ui.views.buttons.CustomShadowButton

@Composable
fun MatchWordView(
    modifier: Modifier, word: MatchViewData, onWordClicked: (MatchViewData) -> Unit = {}
) {
    when (word) {
        is MatchViewData.MatchWordData -> {
            AnimatedVisibility(
                visible = !word.isMatched,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                CustomShadowButton(
                    modifier = modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .padding(8.dp),
                    isEnabled = !word.isSelected,
                    text = word.text,
                    onClick = { onWordClicked(word) }
                )
            }
        }

        is MatchViewData.MatchEmpty -> {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(50.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(whiteColor)
                        .border(
                            1.dp,
                            color = darkColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(30.dp)
                        )
                )
            }
        }
    }
}

@Preview
@Composable
fun MatchWordPreview() {
    AppTheme {
        Scaffold {
            Box(modifier = Modifier.padding(it)) {
                GridWithButtons(testTranslationData, testOriginalData)
            }
        }
    }
}