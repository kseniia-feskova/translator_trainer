package presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.translatortrainer.shared.R
import com.presentation.ui.AppTheme
import com.presentation.ui.darkColor
import com.presentation.ui.primaryColor
import com.presentation.ui.views.FlippableCard
import com.presentation.ui.views.SwipeCard
import presentation.model.WordUI
import presentation.test.smallList
import presentation.ui.AppTypography
import presentation.ui.views.buttons.CustomShadowButton


// Используется только на предпросмотре набора слов,
// где можно посмотреть перевод по клику
// и свайпнуть карточку, если уже ее знаешь
@Composable
fun CardsSet(
    modifier: Modifier = Modifier,
    cardHeight: Dp = 250.dp,
    firstWordUI: WordUI,
    secondWordUI: WordUI?,
    onRightSwipe: (WordUI) -> Unit = {},
    onLeftSwipe: (WordUI) -> Unit = {},
    flipEnabled: Boolean = true,
    swipeEnabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
    ) {

        if (secondWordUI != null) {
            Box(
                modifier = Modifier
                    .height(cardHeight)
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .background(
                        color = darkColor.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .zIndex(2f),
            ) {
                Text(
                    text = secondWordUI.originalText,
                    modifier = Modifier
                        .graphicsLayer {
                            renderEffect = BlurEffect(
                                radiusX = 15f,
                                radiusY = 15f
                            )
                        }
                        .align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge,
                    color = primaryColor,
                    fontSize = TextUnit(22f, TextUnitType.Sp)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .height(cardHeight)
                    .align(Alignment.Center)
                    .padding(8.dp)
                    .fillMaxWidth()
                    .zIndex(2f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = stringResource(R.string.sorted_words),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    style = AppTypography.titleSmall.copy(color = darkColor),
                    fontSize = TextUnit(22f, TextUnitType.Sp)
                )
                CustomShadowButton(
                    onClick = {}, text = stringResource(R.string.sort_again_btn),
                )
            }
        }

        SwipeCard(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(cardHeight)
                .fillMaxWidth()
                .zIndex(3f)
                .align(Alignment.BottomCenter),
            onSwipeRight = {
                onRightSwipe(firstWordUI)
            },
            onSwipeLeft = {
                onLeftSwipe(firstWordUI)
            },
            swipeEnabled = swipeEnabled
        ) {
            FlippableCard(
                word = firstWordUI,
                flipEnabled = flipEnabled
            )
        }

    }
}

@Preview(backgroundColor = 0xFF525552, showBackground = true)
@Composable
fun CardSetPreview() {
    AppTheme {
        Surface {
            CardsSet(
                modifier = Modifier.padding(8.dp),
                firstWordUI = smallList.first(),
                secondWordUI = smallList.lastOrNull(),
            )
        }
    }
}
