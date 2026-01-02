package presentation.ui.screens.lesson.match.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.ui.AppTheme
import presentation.ui.screens.lesson.match.GridWithButtons
import presentation.ui.screens.lesson.match.model.MatchViewData
import presentation.ui.screens.lesson.match.model.testOriginalData
import presentation.ui.screens.lesson.match.model.testTranslationData
import presentation.ui.views.buttons.CustomShadowButton

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
                CustomShadowButton(
                    modifier = modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .padding(8.dp)
                        .alpha(0.1f),
                    isEnabled = false,
                    text = "",
                    onClick = { }
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