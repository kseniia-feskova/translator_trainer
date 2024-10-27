package com.example.translatortrainer.ui.screens.set

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.translatortrainer.test.TestDataHelper
import com.example.translatortrainer.test.model.Word
import com.example.translatortrainer.utils.getLocalizedString
import com.example.translatortrainer.ui.accentColor50
import com.example.translatortrainer.ui.accentColor80
import com.example.translatortrainer.ui.primaryColor
import java.util.Locale

@Composable
fun CardsSet(
    modifier: Modifier = Modifier,
    cardHeight: Dp = 250.dp,
    firstWordUI: Word,
    secondWordUI: Word?,
    showNextPair: () -> Unit = {},
    onRightSwipe: (Word) -> Unit = {},
    onLeftSwipe: (Word) -> Unit = {}
) {
    val context = LocalContext.current

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
                    .background(color = accentColor50, shape = RoundedCornerShape(24.dp))
                    .zIndex(2f),
            ) {
                Text(
                    text = when (secondWordUI) {
                        is Word.WordUI -> secondWordUI.originalText
                        is Word.WordUIFromRes -> context.getLocalizedString(
                            secondWordUI.textRes,
                            Locale("de")
                        )
                    },
                    modifier = Modifier
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

        SwipeCard(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(cardHeight)
                .fillMaxWidth()
                .zIndex(3f)
                .align(Alignment.BottomCenter),
            onSwipeRight = {
                onRightSwipe(firstWordUI)
                showNextPair()
            },
            onSwipeLeft = {
                onLeftSwipe(firstWordUI)
                showNextPair()
            }
        ) {
            FlippableCard(
                frontText = when (firstWordUI) {
                    is Word.WordUI -> firstWordUI.originalText
                    is Word.WordUIFromRes -> context.getLocalizedString(
                        firstWordUI.textRes,
                        Locale("de")
                    )
                },
                backText = when (firstWordUI) {
                    is Word.WordUI -> firstWordUI.resText
                    is Word.WordUIFromRes -> context.getLocalizedString(
                        firstWordUI.textRes,
                        Locale("ru")
                    )
                },
            )
        }

    }
}

@Preview(backgroundColor = 0xFF525552, showBackground = true)
@Composable
fun CardSetPreview() {
    CardsSet(
        modifier = Modifier.padding(8.dp),
        firstWordUI = TestDataHelper().smallList[0],
        secondWordUI = TestDataHelper().smallList[1],
    )
}