package com.example.translatortrainer.ui.screens.main.bottom

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.data.mock.model.SetOfCards
import com.data.model.SetLevel
import com.example.translatortrainer.ui.accentColor
import com.example.translatortrainer.ui.accentSecondColor
import com.example.translatortrainer.ui.primaryColor
import com.example.translatortrainer.ui.secondaryColor

val mockSetOfCard = SetOfCards(
    0,
    "Набор",
    SetLevel.EASY,
    emptySet()
)

@Composable
fun BottomView(
    modifier: Modifier = Modifier,
    set: SetOfCards = mockSetOfCard,
    backgroundColor: Color = Color(0xFFFDFDFD),
    onDeckSelect: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier.then(modifier)
            .clickable {
                Log.e("BorromView", "Set ${set.id} , ${set.title}")
                onDeckSelect(set.id)
            }
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(
                    topStart = 32.dp,
                    topEnd = 32.dp
                )
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 24.dp)
                .padding(top = 18.dp)
        ) {
            Text(
                set.title,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 12.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = TextUnit(22f, TextUnitType.Sp)
            )

            Box(
                modifier = Modifier
                    .background(primaryColor, shape = RoundedCornerShape(16.dp))
                    .align(Alignment.Start)
                    .padding(vertical = 4.dp, horizontal = 12.dp)
            ) {
                Text(
                    "${set.setOfWords.size} слов",
                    modifier = Modifier
                        .align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge,
                    color = secondaryColor,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                )
            }
        }
    }
}

@Composable
fun ThreeBottomView(
    modifier: Modifier,
    setsOfAllCards: SetOfCards? = null,
    setsOfNewCards: SetOfCards? = null,
    setsOfCurrentCards: SetOfCards? = null,
    onDeckSelect: (Int) -> Unit = {}
) {
    Log.e("THree Bottom View", "SetsOfCrads = $setsOfAllCards")
    Box(modifier = Modifier.then(modifier)) {
        if (setsOfCurrentCards != null) {
            BottomView(
                modifier = Modifier.fillMaxHeight()
                    .zIndex(1f),
                set = setsOfCurrentCards,
                backgroundColor = accentSecondColor,
                onDeckSelect = onDeckSelect,
            )
        }
        if (setsOfNewCards != null) {
            BottomView(
                set = setsOfNewCards,
                modifier = Modifier.fillMaxHeight()
                    .padding(top = 64.dp)
                    .zIndex(2f),
                backgroundColor = accentColor,
                onDeckSelect = onDeckSelect,
            )
        }
        if (setsOfAllCards != null) {
            BottomView(
                set = setsOfAllCards,
                modifier = Modifier.fillMaxHeight()
                    .padding(top = 128.dp)
                    .zIndex(3f),
                backgroundColor = secondaryColor,
                onDeckSelect = onDeckSelect,
            )
        }
    }
}

val listOfBottomView = listOf(
    BottomInfo(
        set = mockSetOfCard.copy(title = " Продолжить"),
        backgroundColor = accentSecondColor
    ),
    BottomInfo(
        set = mockSetOfCard.copy(title = "Новые слова"),
        priority = 1f,
        backgroundColor = accentColor
    ),
    BottomInfo(
        set = mockSetOfCard.copy(title = "Все слова"),
        priority = 2f,
        backgroundColor = secondaryColor
    )
)

data class BottomInfo(
    val set: SetOfCards = mockSetOfCard,
    val backgroundColor: Color,
    val priority: Float = 0f,
    val dimen: Dp = 64.dp
)

@Preview(backgroundColor = 0xFF271460, showBackground = true)
@Composable
fun BottomViewsPreview() {
    ThreeBottomView(modifier = Modifier.height(450.dp), mockSetOfCard, mockSetOfCard, mockSetOfCard)
}

private class PreviewProvider : PreviewParameterProvider<BottomInfo> {
    override val values: Sequence<BottomInfo>
        get() = listOfBottomView.asSequence()
}

@Preview(backgroundColor = 0xFFFFFFF, showBackground = true)
@Composable
fun BottomViewPreview(@PreviewParameter(PreviewProvider::class) state: BottomInfo) {
    Box(modifier = Modifier.padding(vertical = 16.dp)) {
        BottomView(
            set = state.set,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = state.dimen * state.priority)
                .zIndex(state.priority),
            backgroundColor = state.backgroundColor
        )
    }
}