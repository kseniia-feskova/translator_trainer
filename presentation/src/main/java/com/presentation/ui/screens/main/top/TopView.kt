package com.presentation.ui.screens.main.top

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.ui.AppTheme
import com.presentation.ui.primaryColor
import com.presentation.ui.secondaryColor
import com.presentation.ui.surfaceLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val allWordsCount = 30

@Composable
fun TopView(
    countOfWords: Int = 23,
    donutSize: Dp = 160.dp,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = secondaryColor,
                shape = RoundedCornerShape(
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            ),
    ) {

        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            LinearDeterminateIndicator(
                modifier = Modifier
                    .size(donutSize)
                    .padding(16.dp),
                value = countOfWords,
                maxValue = 30
            )
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    "Выучено слов сегодня",
                    style = MaterialTheme.typography.bodyLarge,
                    color = primaryColor,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "$countOfWords из $allWordsCount",
                    fontWeight = FontWeight.Medium,
                    color = primaryColor,
                    fontSize = TextUnit(24f, TextUnitType.Sp)
                )
            }
        }
    }
}

@Composable
fun LinearDeterminateIndicator(modifier: Modifier = Modifier, value: Int, maxValue: Int) {
    var currentProgress by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope() // Create a coroutine scope
    LaunchedEffect(Unit) {
        scope.launch {
            delay(300L)
            loadProgress(value, maxValue) { progress ->
                currentProgress = progress
            }
        }
    }
    Box() {

        //  if (loading) {
        CircularProgressIndicator(
            progress = { currentProgress },
            modifier = Modifier
                .then(modifier)
                .align(Alignment.Center),
            strokeWidth = 8.dp,
            trackColor = surfaceLight,
            color = primaryColor,
            gapSize = 0.dp
        )
        Text(
            text = (value * 100 / maxValue).toString() + "%",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodyLarge,
            fontSize = TextUnit(22f, TextUnitType.Sp),
            color = primaryColor,
        )
    }
}

/** Iterate the progress value */
suspend fun loadProgress(value: Int, maxValue: Int, updateProgress: (Float) -> Unit) {
    for (i in 1..value) {
        updateProgress(i.toFloat() / maxValue)
        delay(10)
    }
}

@Preview
@Composable
fun LinearDeterminateIndicatorPreview() {
    AppTheme {
        Surface {
            LinearDeterminateIndicator(value = 8, maxValue = 10)
        }
    }
}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
fun TopViewPreview() {
    TopView()
}
