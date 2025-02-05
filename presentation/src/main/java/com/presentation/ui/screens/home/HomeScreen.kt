package com.presentation.ui.screens.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.presentation.R
import com.presentation.navigation.BottomNavigationBar
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.bgColor
import com.presentation.ui.darkColor
import com.presentation.ui.fieldBorderColor
import com.presentation.ui.fieldColors
import com.presentation.ui.fieldValueColor
import com.presentation.ui.lightLilaColor
import com.presentation.ui.screens.auth.CustomShadowButton
import com.presentation.ui.views.BackgroundDecorAnimated
import com.presentation.ui.views.HomeTopView
import com.presentation.ui.views.Loader
import com.presentation.ui.whiteColor
import com.presentation.utils.Language

@Composable
fun HomeScreen(
    state: HomeUIState,
    onWordInput: (String) -> Unit = {},
    onEnterText: () -> Unit = { },
    onSaveClick: () -> Unit = {},
    onLanguageChange: (Language) -> Unit = {}
) {
    var showTopView by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) { showTopView = true }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackgroundDecorAnimated()
        AnimatedVisibility(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.TopCenter),
            visible = showTopView,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(1000)
            ),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            HomeTopView(stringResource(R.string.home_title))
        }

        Column(
            modifier = Modifier
                .background(
                    color = whiteColor.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(24.dp)
                )
                .align(Alignment.Center)
        ) {
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                value = state.inputText,
                onValueChange = { onWordInput(it) },
                singleLine = true,
                label = { Text(state.originalLanguage.name, style = AppTypography.titleSmall) },
                shape = RoundedCornerShape(8.dp),
                colors = fieldColors(),
                textStyle = AppTypography.titleSmall,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // Изменяем кнопку на "Готово"
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onEnterText()
                        keyboardController?.hide() // Скрываем клавиатуру
                    }
                )
            )

            Spacer(Modifier.height(24.dp))

            LanguageSwitch(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                state.originalLanguage,
                state.resLanguage
            ) { onLanguageChange(it) }

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                value = state.translatedText,
                enabled = false,
                onValueChange = {},
                singleLine = true,
                label = {
                    Text(
                        state.resLanguage.name,
                        style = AppTypography.titleSmall,
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = fieldColors().copy(
                    disabledTextColor = fieldValueColor,
                    disabledContainerColor = Color.White,
                    disabledIndicatorColor = fieldBorderColor
                ),
                textStyle = AppTypography.titleSmall,
                trailingIcon = {
                    if (state.isWordSaved) {
                        Icon(Icons.Default.Save, tint = bgColor, contentDescription = "Saved")
                    }
                }
            )
            Spacer(Modifier.height(24.dp))

        }
        CustomShadowButton(
            text = stringResource(R.string.save_word_btn),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            onClick = {
                if (state.translatedText.isNotEmpty()) {
                    onSaveClick()
                }
            }
        )

        if (state.loading) {
            Loader(
                modifier = Modifier
                    .width(80.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun LanguageSwitch(
    modifier: Modifier,
    originLang: Language,
    translateLang: Language,
    onClick: (Language) -> Unit
) {
    // Фиксируем первоначальные названия языков при первом рендере
    val firstText = remember { originLang.name }
    val secondText = remember { translateLang.name }
    var selectedLanguage by remember { mutableStateOf(firstText) }
    val transition = updateTransition(targetState = selectedLanguage, label = "Language Transition")

    val backgroundOffset by transition.animateDp(label = "Background Offset") { language ->
        if (language == firstText) 0.dp else (firstText.length * 14f).dp // Смещение фона
    }

    val backgroundWidth by transition.animateDp(label = "Background Width") { language ->
        if (language == firstText) (firstText.length * 13f).dp else (secondText.length * 13f).dp  // Можно подстроить под разную длину слов
    }

    Box(
        modifier = Modifier
            .then(modifier)
            .background(whiteColor, shape = RoundedCornerShape(8.dp))
            .wrapContentSize()
            .clickable { /* Игнорируем клики по Box, обработка ниже */ },
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = backgroundOffset)
                .width(backgroundWidth)
                .height(40.dp)
                .background(lightLilaColor, shape = RoundedCornerShape(8.dp))
        )

        Row(
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Text(
                text = firstText,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable {
                        selectedLanguage = firstText
                        onClick(if (originLang.name == firstText) originLang else translateLang)
                    },
                color = darkColor,
                style = AppTypography.displayLarge.copy(fontSize = TextUnit(24f, TextUnitType.Sp))
            )

            Spacer(Modifier.width(8.dp))
            Text(
                text = secondText,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable {
                        selectedLanguage = secondText
                        onClick(if (translateLang.name == secondText) translateLang else originLang)
                    },
                color = darkColor,
                style = AppTypography.displayLarge.copy(fontSize = TextUnit(24f, TextUnitType.Sp))
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    AppTheme {
        Surface {
            HomeScreen(state = HomeUIState(inputText = "Katze"))
        }
    }
}

@Preview
@Composable
fun MainScreenWithButtonPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("Preview", "paddings $paddings")
            Box(modifier = Modifier.padding(paddings)) {
                HomeScreen(
                    state = HomeUIState().copy(
                        inputText = "Katze",
                        translatedText = "Котик"
                    )
                )
            }
        }, bottomBar = {
            val context = LocalContext.current
            BottomNavigationBar(navController = NavController(context))
        }
        )
    }
}
