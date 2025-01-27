package com.example.translatortrainer.ui.screens.set

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.translatortrainer.test.model.Level
import com.example.translatortrainer.test.model.Word
import com.example.translatortrainer.ui.accentColor
import com.example.translatortrainer.ui.core.ActionButton
import com.example.translatortrainer.ui.primaryColor
import com.example.translatortrainer.viewmodel.CardSetState

@Composable
fun CardSetScreen(
    state: CardSetState,
    addWordToKnow: (Word) -> Unit = {},
    addWordToLearn: (Word) -> Unit = {},
    startCourse: () -> Unit = {}
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val cardHeight = screenHeight * 0.25f

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(color = primaryColor)
        ) {
            TopViewCardSet(state.knowWords, state.allWords)

            if (state.words != null) {
                CardsSet(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp),
                    cardHeight = cardHeight,
                    state.words.first,
                    state.words.second,
                    onRightSwipe = addWordToKnow,
                    onLeftSwipe = addWordToLearn
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(top = cardHeight * 1.33f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Не знаю", color = accentColor,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = TextUnit(16f, TextUnitType.Sp)
                    )
                    Text(
                        text = "Знаю", color = accentColor,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = TextUnit(16f, TextUnitType.Sp),
                    )
                }
            }

            ActionButton(
                onClick = { startCourse() },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Изучить набор",
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = primaryColor,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = TextUnit(16f, TextUnitType.Sp),
                )
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000999)
@Composable
fun CardSetScreenPreview() {
    CardSetScreen(
        state = CardSetState(
            words = Pair(
                Word.WordUI(
                    "Deutches Wort",
                    "Немецкое слово",
                    Level.NEW
                ), null
            )
        )
    )
}
