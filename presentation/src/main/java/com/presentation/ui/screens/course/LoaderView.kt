package com.presentation.ui.screens.course

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.ui.primaryColor
import com.presentation.ui.secondaryColor
import kotlinx.coroutines.delay

@Composable
fun LoaderView(
    modifier: Modifier = Modifier,
    text: String = "",
    subtext: String = "",
    showContinueBtn: Boolean = false,
    onButtonClick: () -> Unit = {}
) {
    val firstTextVisible = remember { mutableStateOf(false) }
    val secondTextVisible = remember { mutableStateOf(false) }
    val showContinueBtnVisible = remember { mutableStateOf(showContinueBtn) }

    // Запускаем анимацию для первого текста, затем второго с задержкой
    LaunchedEffect(Unit) {
        firstTextVisible.value = true
        delay(600)  // Задержка перед появлением второго текста
        secondTextVisible.value = true
        delay(600)
        //   showContinueBtnVisible.value = true
    }

    Column(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        AnimatedVisibility(
            visible = firstTextVisible.value,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) // Плавное появление
        ) {
            Text(
                text = text, style = MaterialTheme.typography.bodyLarge,
                color = secondaryColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = TextUnit(24f, TextUnitType.Sp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = secondTextVisible.value,
            enter = fadeIn(animationSpec = tween(durationMillis = 1600)) // Плавное появление
        ) {
            Text(
                text = subtext, style = MaterialTheme.typography.bodyLarge,
                color = secondaryColor,
                fontSize = TextUnit(18f, TextUnitType.Sp)
            )
        }
        Spacer(Modifier.height(16.dp))
        AnimatedVisibility(
            visible = showContinueBtnVisible.value,
            enter = fadeIn(animationSpec = tween(durationMillis = 3500)) // Плавное появление
        ) {
            Button(onClick = { onButtonClick() }) {
                Text(
                    "Далее"
                )
            }
        }
    }

}


@Preview
@Composable
fun LoaderViewPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryColor)
    ) {
        LoaderView(
            modifier = Modifier.align(Alignment.Center),
            text = "Задание №1",
            subtext = "Выберите перевод"
        )
    }
}