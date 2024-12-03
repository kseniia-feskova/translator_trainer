package com.presentation.ui.views

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.core.ActionButton
import com.presentation.ui.indicatorColorLight
import com.presentation.ui.onPrimaryColorLight
import com.presentation.ui.onSurfaceLight
import com.presentation.ui.primaryColorLight
import com.presentation.ui.screens.home.HomeUIState
import com.presentation.utils.Language
import kotlinx.coroutines.launch

@Composable
fun TranslateView(
    modifier: Modifier = Modifier,
    state: HomeUIState = HomeUIState(),
    onTextChange: (String) -> Unit = {},
    onEnterText: (String) -> Unit = { },
    onLanguageChange: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var isRussianOriginLanguage by remember { mutableStateOf(state.originalLanguage == Language.RUSSIAN) }
    val coroutineScope = rememberCoroutineScope()

    val topFieldOffset by animateDpAsState(
        if (isRussianOriginLanguage) 140.dp else 0.dp,
        animationSpec = tween(600)
    )
    val bottomFieldOffset by animateDpAsState(
        if (isRussianOriginLanguage) (-140).dp else 0.dp,
        animationSpec = tween(600)
    )

    // Анимируем масштаб полей
    val topFieldScale = remember { Animatable(1f) }
    val bottomFieldScale = remember { Animatable(1f) }

    // Запуск анимации изменения масштаба при изменении `isRussianOriginLanguage`
    LaunchedEffect(isRussianOriginLanguage) {
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
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
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
                style = MaterialTheme.typography.titleLarge,
            )

            TranslateInput(
                isOriginLanguage = !isRussianOriginLanguage,
                text = if (!isRussianOriginLanguage) state.inputText else state.translatedText,
                savedWord = state.savedWord,
                onTextChange = onTextChange,
                onEnterText = {
                    keyboardController?.hide()
                    onEnterText(it)
                },
                onSaveClick = onSaveClick
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Icon(
            Icons.AutoMirrored.Filled.CompareArrows,
            contentDescription = "Change",
            tint = onSurfaceLight,
            modifier = Modifier
                .rotate(90f)
                .padding(8.dp)
                .scale(1.5f)
                .clickable(indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    isRussianOriginLanguage = !isRussianOriginLanguage
                    onLanguageChange()
                },

            )

        Column(
            modifier = Modifier
                .scale(bottomFieldScale.value)
                .offset(y = bottomFieldOffset)
                .zIndex(0f)
        ) {
            Text(
                text = "Русский",
                style = AppTypography.titleLarge,
            )
            TranslateInput(
                isOriginLanguage = isRussianOriginLanguage,
                text = if (isRussianOriginLanguage) state.inputText else state.translatedText,
                savedWord = state.savedWord,
                onTextChange = onTextChange,
                onEnterText = {
                    keyboardController?.hide()
                    onEnterText(it)
                },
                onSaveClick = onSaveClick
            )
        }
    }
}

private val listOfStates =
    listOf(HomeUIState(), HomeUIState(inputText = "Katze"), HomeUIState(inputText = "Katze", translatedText = "Кошка"))

private class PreviewProvider : PreviewParameterProvider<HomeUIState> {
    override val values: Sequence<HomeUIState>
        get() = listOfStates.asSequence()
}

@Preview()
@Composable
fun TranslateViewPreview(@PreviewParameter(PreviewProvider::class) state: HomeUIState) {
    AppTheme {
        Surface {

            Box(modifier = Modifier.padding(24.dp)) {
                TranslateView(state = state)
            }
        }
    }
}

@Composable
fun TranslateInput(
    isOriginLanguage: Boolean,
    text: String,
    savedWord: Boolean,
    onTextChange: (String) -> Unit,
    onEnterText: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds()
            .shadow(
                elevation = 18.dp,
                clip = false,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.Transparent,
                spotColor = Color.Transparent
            ),
        value = text,
        readOnly = !isOriginLanguage,
        onValueChange = {
            onTextChange(it)
        },
        colors = TextFieldDefaults.colors().copy(
            cursorColor = onSurfaceLight,
            unfocusedIndicatorColor = indicatorColorLight,
            unfocusedContainerColor = primaryColorLight,
            focusedIndicatorColor = indicatorColorLight,
            focusedContainerColor = primaryColorLight,
            unfocusedTextColor = onPrimaryColorLight,
            focusedTextColor = onPrimaryColorLight
        ),
        textStyle = AppTypography.bodyLarge,
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onEnterText(text)
            }
        ),
        trailingIcon = {
            if (text.isNotEmpty() && !isOriginLanguage) {
                Icon(
                    modifier = Modifier.clickable { if (!savedWord) onSaveClick() },
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save",
                    tint = if(savedWord) MaterialTheme.colorScheme.onPrimary else indicatorColorLight
                )
            }
        }
    )
}


@Preview()
@Composable
fun TranslateViewNewPreview() {
    AppTheme {
        Surface {
            var isOriginLanguage by remember { mutableStateOf(true) }
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TranslateInput(
                    isOriginLanguage = isOriginLanguage,
                    text = "Wort",
                    savedWord = false,
                    onTextChange = {},
                    onEnterText = {},
                    {}
                )

                TranslateInput(
                    isOriginLanguage = !isOriginLanguage,
                    text = "Слово",
                    savedWord = false,
                    onTextChange = { },
                    onEnterText = {},
                    {}
                )

                ActionButton(onClick = { isOriginLanguage = !isOriginLanguage }) {
                    Text("Swipe")
                }
            }
        }
    }
}
