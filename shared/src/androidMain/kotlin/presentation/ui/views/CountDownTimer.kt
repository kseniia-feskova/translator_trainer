package com.presentation.ui.views

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import presentation.ui.AppTypography
import com.presentation.ui.bgColor
import kotlinx.coroutines.delay

@SuppressLint("DefaultLocale")
@Composable
fun CountdownTimer(
    totalTime: Long = 60_000L, // 1 минута в миллисекундах
    interval: Long = 1_000L, // 1 секунда
    onTimeOver: () -> Unit
) {
    var timeLeft by remember { mutableLongStateOf(totalTime) }
    var isRunning by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = isRunning) {
        if (isRunning) {
            while (timeLeft > 0) {
                delay(interval)
                timeLeft -= interval
            }
            isRunning = false
        } else {
            onTimeOver()
        }
    }

    val minutes = (timeLeft / 1000) / 60
    val seconds = (timeLeft / 1000) % 60
    val timeText = String.format("%02d:%02d", minutes, seconds)

    Text(
        text = timeText,
        color = bgColor,
        style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold)
    )
}
