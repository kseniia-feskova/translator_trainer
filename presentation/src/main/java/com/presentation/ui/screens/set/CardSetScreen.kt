package com.presentation.ui.screens.set

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.zIndex
import com.presentation.model.Level
import com.presentation.model.WordUI
import com.presentation.ui.accentColor
import com.presentation.ui.core.ActionButton
import com.presentation.ui.core.SecondButton
import com.presentation.ui.primaryColor
import com.presentation.ui.secondaryColor
import com.presentation.viewmodel.CardSetState
import java.util.Date

@Composable
fun CardSetScreen(
    state: CardSetState,
    addWordToKnow: (WordUI) -> Unit = {},
    addWordToLearn: (WordUI) -> Unit = {},
    resetCardSet: () -> Unit = {},
    startCourse: () -> Unit = {}
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val cardHeight = screenHeight * 0.25f

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = primaryColor)
        ) {
            TopViewCardSet(state.knowWords, state.allWords, name = state.name)

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
            } else {
                Column(
                    modifier = Modifier
                        .height(cardHeight)
                        .align(Alignment.Center)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .zIndex(2f),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Вы отсортировали все слова",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.bodyLarge,
                        color = secondaryColor,
                        fontSize = TextUnit(22f, TextUnitType.Sp)
                    )
                    SecondButton(onClick = { resetCardSet() }) {
                        Text(
                            text = "Отсортировать заново",
                            modifier = Modifier.padding(vertical = 8.dp),
                        )
                    }
                }
            }

            ActionButton(
                onClick = { startCourse() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
                    .padding(horizontal = 16.dp)
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
                WordUI(
                    0, "Deutches Wort",
                    "Немецкое слово",
                    level = Level.NEW,
                    date = Date()
                ), null
            )
        )
    )
}


@Preview(showBackground = true, backgroundColor = 0xFF000999)
@Composable
fun EmptyCardSetScreenPreview() {
    CardSetScreen(
        state = CardSetState(
            words = null
        )
    )
}