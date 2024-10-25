package com.example.translatortrainer.ui.top

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.translatortrainer.ui.primaryColor
import com.example.translatortrainer.ui.secondaryColor

val allWordsCount = 30

@Composable
fun TopView(
    countOfWords: Int = 15,
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
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 8.dp)
        ) {
            Icon(
                Icons.Default.Menu,
                contentDescription = "Menu",
                Modifier
                    .background(primaryColor, shape = CircleShape)
                    .padding(8.dp)
                    .clickable { },
                tint = secondaryColor
            )
        }

        Row(
            modifier = Modifier
                .padding(end = 20.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly

        ) {
            DonutChart(
                percentage = (countOfWords.toFloat() / allWordsCount) * 100,
                donutSize = donutSize,
                strokeWidth = donutSize.value*0.2f,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterVertically),
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
                    fontSize = TextUnit(18f, TextUnitType.Sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "$countOfWords из $allWordsCount",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryColor,
                    fontSize = TextUnit(30f, TextUnitType.Sp)
                )
            }
        }
    }
}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
fun TopViewPreview() {
    TopView()
}
