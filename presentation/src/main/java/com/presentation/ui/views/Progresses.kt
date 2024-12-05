package com.presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.ui.AppTheme
import com.presentation.ui.accentColor
import com.presentation.ui.secondaryColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Loader(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onPrimary,
        strokeWidth = 6.dp,
        trackColor = MaterialTheme.colorScheme.primary,
    )
}

@Composable
fun CircleProgress(modifier: Modifier = Modifier, value: Int, maxValue: Int) {
    var currentProgress by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            delay(300L)
            loadProgress(value, maxValue) { progress ->
                currentProgress = progress
            }
        }
    }
    Box() {

        CircularProgressIndicator(
            progress = { currentProgress },
            modifier = Modifier
                .then(modifier)
                .align(Alignment.Center),
            strokeWidth = 8.dp,
            trackColor = MaterialTheme.colorScheme.surface,
            color = MaterialTheme.colorScheme.onSurface,
            gapSize = 0.dp
        )
        Text(
            text = (value * 100 / maxValue).toString() + "%",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
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

@Composable
fun ProgressForSet(
    modifier: Modifier = Modifier,
    current: Int,
    all: Int
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Вы знаете $current из $all слов", style = MaterialTheme.typography.titleMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(8.dp)
                )
                .height(14.dp)
        ) {
            val width = (current.toFloat() / all)
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .fillMaxWidth(width)
            )

        }
    }
}

@Composable
fun ProgressForLesson(
    modifier: Modifier = Modifier,
    current: Int = 0,
    all: Int = 5
) {
    Box() {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .then(modifier)
                .fillMaxWidth()
                .background(color = secondaryColor, shape = RoundedCornerShape(8.dp))
                .wrapContentHeight()
        ) {
            val width = (current.toFloat() / all)
            Spacer(
                modifier = Modifier
                    .height(14.dp)
                    .background(color = accentColor, shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth(width)
            )
        }
        DynamicRowExample(current, all)
    }
}


@Composable
fun DynamicRowExample(current: Int, all: Int) {
    // Рисуем строку с заданным количеством элементов
    DynamicRow(current, itemCount = all)

}

@Composable
fun DynamicRow(current: Int, itemCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(itemCount) { index ->
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        if (index < current) accentColor else secondaryColor,
                        shape = RoundedCornerShape(24.dp)
                    )
            )
        }
    }
}

@Preview
@Composable
fun CircleProgressPreview() {
    AppTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Row() {
                    CircleProgress(
                        modifier = Modifier
                            .size(180.dp)
                            .padding(24.dp),
                        value = 3,
                        maxValue = 10
                    )

                    Loader(
                        Modifier
                            .width(80.dp)
                            .align(Alignment.CenterVertically)
                    )
                }

                ProgressForSet(
                    modifier = Modifier
                        .padding(12.dp), current = 16, all = 80
                )

                ProgressForLesson(
                    modifier = Modifier
                        .padding(12.dp), current = 6, all = 8
                )
            }
        }
    }
}