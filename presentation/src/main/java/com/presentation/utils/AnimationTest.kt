package com.presentation.utils

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun SwapTextFields() {
    var isSwapped by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val topFieldOffset by animateDpAsState(
        if (isSwapped) 110.dp else 0.dp,
        animationSpec = tween(600)
    )
    val bottomFieldOffset by animateDpAsState(
        if (isSwapped) (-110).dp else 0.dp,
        animationSpec = tween(600)
    )


    // Анимируем масштаб верхнего поля
    val topFieldScale = remember { Animatable(1f) }

    // Запуск анимации изменения масштаба при изменении `isSwapped`
    LaunchedEffect(isSwapped) {
        coroutineScope.launch {
            topFieldScale.animateTo(
                targetValue = 1.1f,
                animationSpec = tween(300)
            )
            topFieldScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(300)
            )
        }
    }

    // Анимируем масштаб верхнего поля
    val bottomFieldScale = remember { Animatable(1f) }

    // Запуск анимации изменения масштаба при изменении `isSwapped`
    LaunchedEffect(isSwapped) {
        coroutineScope.launch {
            bottomFieldScale.animateTo(
                targetValue = 0.9f,
                animationSpec = tween(300)
            )
            bottomFieldScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(300)
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Верхнее поле
        TextField(
            value = "Верхнее поле",
            onValueChange = {},
            modifier = Modifier
                .scale(topFieldScale.value)
                .offset(y = topFieldOffset)
                .padding(16.dp)
                .zIndex(1f)
        )

        // Динамическое пространство между полями
        Spacer(modifier = Modifier.height(16.dp))

        // Нижнее поле
        TextField(
            value = "Нижнее поле",
            onValueChange = {},
            modifier = Modifier
                .scale(bottomFieldScale.value)
                .offset(y = bottomFieldOffset)
                .padding(16.dp)
                .zIndex(0f)

        )

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка для запуска анимации
        Button(onClick = { isSwapped = !isSwapped }) {
            Text("Swap Fields")
        }
    }
}

@Preview
@Composable
fun SwapTextFieldsPreview() {
    SwapTextFields()
}