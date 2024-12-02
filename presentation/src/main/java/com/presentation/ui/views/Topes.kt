package com.presentation.ui.views


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.ui.AppTheme

private const val MAX_WORDS = 30
@Composable
fun TopView(
    countOfWords: Int = 23,
    donutSize: Dp = 160.dp,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ),
    ) {

        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        ) {

            CircleProgress(
                modifier = Modifier
                    .size(donutSize)
                    .padding(16.dp),
                value = countOfWords,
                maxValue = MAX_WORDS
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    "Выучено слов сегодня",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "$countOfWords из $MAX_WORDS",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
        }
    }
}



@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
fun TopViewPreview() {
    AppTheme {
        Surface {
            TopView()
        }
    }
}