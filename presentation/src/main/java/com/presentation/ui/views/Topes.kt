package com.presentation.ui.views


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.ui.AppTheme

private const val MAX_WORDS = 30

@Composable
fun HomeTopView(
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

@Composable
fun BasicTopView(
    title: String,
    leftIcon: ImageVector? = null,
    rightIcon: ImageVector? = null,
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leftIcon != null) {
            Icon(
                imageVector = leftIcon,
                contentDescription = "LeftIcon",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onLeftClick() }
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
        )

        if (rightIcon != null) {
            Icon(
                imageVector = rightIcon,
                contentDescription = "RightIcon",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onRightClick() }
            )
        } else if (leftIcon != null) {
            Icon(
                imageVector = leftIcon,
                contentDescription = "RightIcon",
                modifier = Modifier.padding(16.dp),
                tint = Color.Transparent
            )
        }
    }
}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
fun TopViewPreview() {
    AppTheme {
        Surface {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                HomeTopView()
                BasicTopView("Новые карточки")
                BasicTopView("Новые карточки",rightIcon = Icons.Default.AddCircle)
                BasicTopView(
                    "Набор карточек №1",
                    leftIcon = Icons.Default.ArrowBackIosNew,
                    rightIcon = Icons.Default.Edit
                )
                BasicTopView(
                    "Статистика",
                    leftIcon = Icons.Default.ArrowBackIosNew,
                )
            }
        }
    }
}