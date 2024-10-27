package com.example.translatortrainer.ui.screens.main.bottom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.translatortrainer.ui.accentColor
import com.example.translatortrainer.ui.accentSecondColor
import com.example.translatortrainer.ui.primaryColor
import com.example.translatortrainer.ui.secondaryColor

@Composable
fun BottomView(
    modifier: Modifier = Modifier,
    text: String = "Новые слова",
    backgroundColor: Color = Color(0xFFFDFDFD),
    onDeckSelect: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .clickable { onDeckSelect(text) }
            .then(modifier)
            .height(300.dp)
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
                text,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 24.dp),
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
                    "28 слов",
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
fun ThreeBottomView(modifier: Modifier, onDeckSelect: (String) -> Unit = {}) {
    Box(modifier) {
        BottomView(
            text = "Продолжить",
            backgroundColor = accentSecondColor,
            onDeckSelect = onDeckSelect
        )
        BottomView(
            modifier = Modifier
                .padding(top = 64.dp)
                .zIndex(1f),  // Низкий z-индекс,
            backgroundColor = accentColor,
            onDeckSelect = onDeckSelect
        )
        BottomView(
            text = "Все слова",
            modifier = Modifier
                .padding(top = 128.dp)
                .zIndex(2f),  // Низкий z-индекс,
            backgroundColor = secondaryColor,
            onDeckSelect = onDeckSelect
        )
    }
}

val listOfBottomView = listOf(
    BottomInfo(text = "Продолжить", backgroundColor = accentSecondColor),
    BottomInfo(
        text = "Новые слова",
        priority = 1f,
        backgroundColor = accentColor
    ),
    BottomInfo(
        text = "Все слова",
        priority = 2f,
        backgroundColor = secondaryColor
    )
)

data class BottomInfo(
    val text: String,
    val backgroundColor: Color,
    val priority: Float = 0f,
    val dimen: Dp = 64.dp
)

@Preview(backgroundColor = 0xFF8C58D4, showBackground = true)
@Composable
fun BottomViewsPreview() {
    ThreeBottomView(modifier = Modifier.height(300.dp))
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
            text = state.text,
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(top = state.dimen * state.priority)
                .zIndex(state.priority),
            backgroundColor = state.backgroundColor
        )
    }
}