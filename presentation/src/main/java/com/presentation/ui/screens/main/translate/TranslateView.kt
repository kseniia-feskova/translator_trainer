package com.presentation.ui.screens.main.translate

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.presentation.ui.core.ActionButton
import com.presentation.ui.screens.main.translate.model.TranslatorState
import com.presentation.ui.secondaryColor
import com.presentation.utils.Language
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TranslateView(
    modifier: Modifier = Modifier,
    state: TranslatorState = TranslatorState(),
    onFinishGlow: () -> Unit = {},
    onTextChange: (String) -> Unit = {},
    onEnterText: (String, Language, Language) -> Unit = { _, _, _ -> },
    onLanguageChange: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    var isSwapped by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val topFieldOffset by animateDpAsState(
        if (isSwapped) 140.dp else 0.dp,
        animationSpec = tween(600)
    )
    val bottomFieldOffset by animateDpAsState(
        if (isSwapped) (-140).dp else 0.dp,
        animationSpec = tween(600),
        finishedListener = { onLanguageChange() }
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

    val targetColor by animateColorAsState(
        targetValue = when {
            state.showGlow -> secondaryColor
            else -> Color.Transparent
        },
        animationSpec = tween(durationMillis = 300), label = ""
    )

    LaunchedEffect(state.showGlow) {
        delay(700)
        onFinishGlow()
    }

    Column(
        modifier = Modifier
            .then(modifier)
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.End
    ) {
        Column(
            modifier = Modifier
                .scale(topFieldScale.value)
                .offset(y = topFieldOffset)
                .zIndex(1f)
        ) {
            Text(
                text = "Немецкий",
                color = Color.White,
                fontSize = TextUnit(18f, TextUnitType.Sp)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clipToBounds()
                    .shadow(
                        elevation = 18.dp,
                        clip = false,
                        shape = RoundedCornerShape(10.dp),
                        ambientColor = if (isSwapped) targetColor else Color.Transparent,
                        spotColor = if (isSwapped) targetColor else Color.Transparent
                    ),
                value = if (isSwapped) state.translatedText else state.inputText,
                onValueChange = {
                    if (!isSwapped) {
                        onTextChange(it)
                    }
                },
                label = { Text("") },
                colors = TextFieldDefaults.colors().copy(
                    disabledContainerColor = secondaryColor,
                    unfocusedContainerColor = secondaryColor,
                    focusedContainerColor = secondaryColor,
                ),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                readOnly = isSwapped,
                keyboardActions = KeyboardActions(
                    onDone = {
                        onEnterText(state.inputText, state.originalLanguage, state.resLanguage)
                    }
                ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Icon(
            Icons.AutoMirrored.Filled.CompareArrows,
            contentDescription = "Change",
            tint = secondaryColor,
            modifier = Modifier
                .rotate(90f)
                .padding(8.dp)
                .scale(1.5f)
                .clickable {
                    isSwapped = !isSwapped
                }
        )

        Column(
            modifier = Modifier
                .scale(bottomFieldScale.value)
                .offset(y = bottomFieldOffset)
                .zIndex(0f)
        ) {
            Text(
                text = "Русский",
                color = Color.White,
                fontSize = TextUnit(18f, TextUnitType.Sp)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 18.dp,
                        clip = false,
                        shape = RoundedCornerShape(10.dp),
                        ambientColor = if (!isSwapped) targetColor else Color.Transparent,
                        spotColor = if (!isSwapped) targetColor else Color.Transparent
                    ),
                value = if (!isSwapped) state.translatedText else state.inputText,
                onValueChange = {
                    if (isSwapped) {
                        onTextChange(it)
                    }
                },
                readOnly = !isSwapped,
                label = { Text("") },
                colors = TextFieldDefaults.colors().copy(
                    disabledContainerColor = secondaryColor,
                    unfocusedContainerColor = secondaryColor,
                    focusedContainerColor = secondaryColor,
                ),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onEnterText(state.inputText, state.originalLanguage, state.resLanguage)
                    }
                ),
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        if (state.translatedText.isNotEmpty() && state.inputText.isNotEmpty()) {
            ActionButton(
                modifier = Modifier.padding(horizontal = 72.dp),
                onClick = { onSaveClick() }) {
                Text("Сохранить")
            }
        } else {
            Spacer(modifier = Modifier.height(56.dp))

        }
    }
}

private val listOfStates =
    listOf(TranslatorState(), TranslatorState("Katze"), TranslatorState("Katze", "Кошка"))

private class PreviewProvider : PreviewParameterProvider<TranslatorState> {
    override val values: Sequence<TranslatorState>
        get() = listOfStates.asSequence()
}

@Preview(backgroundColor = 0xFF5633D1, showBackground = true)
@Composable
fun TranslateViewPreview(@PreviewParameter(PreviewProvider::class) state: TranslatorState) {
    Box(modifier = Modifier.padding(vertical = 12.dp)) {
        TranslateView(state = state)
    }
}