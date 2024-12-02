package com.presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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

@Preview
@Composable
fun CircleProgressPreview() {
    AppTheme {
        Surface {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)) {
                CircleProgress(
                    modifier = Modifier
                        .size(180.dp)
                        .padding(24.dp),
                    value = 3,
                    maxValue = 10
                )
            }
        }
    }
}