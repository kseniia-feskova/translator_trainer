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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.presentation.navigation.BottomNavigationBar
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.darkColor
import com.presentation.ui.fieldColors
import com.presentation.ui.lightLilaColor
import com.presentation.ui.screens.auth.CustomShadowButton
import com.presentation.ui.views.BackgroundDecorAnimated
import com.presentation.ui.views.HomeTopView
import com.presentation.ui.views.Loader
import com.presentation.ui.whiteColor

@Composable
fun HomeScreen(
    state: HomeUIState,
    onWordInput: (String) -> Unit = {},
    onEnterText: (String) -> Unit = { },
    onSaveClick: () -> Unit = {},
    onLanguageChange: () -> Unit = {}
) {
    var showTopView by remember { mutableStateOf(false) }

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
            HomeTopView("Let's translate words")
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .align(Alignment.Center)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                singleLine = true,
                label = { Text("French", style = AppTypography.titleSmall) },
                shape = RoundedCornerShape(8.dp),
                colors = fieldColors(),
                textStyle = AppTypography.titleSmall,
                // isError = state.error == AuthError.EMPTY_FIELDS && state.email.isEmpty()
            )

            Spacer(Modifier.height(24.dp))

            LanguageSwitch(modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                singleLine = true,
                label = {
                    Text(
                        "Russian",
                        style = AppTypography.titleSmall,
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = fieldColors(),
                textStyle = AppTypography.titleSmall
            )

        }
        CustomShadowButton(
            "Save word",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        )

//        TranslateView(
//            modifier = Modifier
//                .padding(24.dp)
//                .align(Alignment.Center),
//            state = state,
//            onTextChange = onWordInput,
//            onEnterText = onEnterText,
//            onSaveClick = onSaveClick,
//            onLanguageChange = onLanguageChange
//        )
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
fun LanguageSwitch(modifier: Modifier) {
    var selectedLanguage by remember { mutableStateOf("French") }
    val transition = updateTransition(targetState = selectedLanguage, label = "Language Transition")

    val backgroundOffset by transition.animateDp(label = "Background Offset") { language ->
        if (language == "French") 0.dp else ("French".length * 14f).dp // Смещение фона
    }

    val backgroundWidth by transition.animateDp(label = "Background Width") { language ->
        if (language == "French") ("French".length * 13f).dp else ("RUSSIAN".length * 13f).dp  // Можно подстроить под разную длину слов
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
                text = "French",
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable { selectedLanguage = "French" },
                color = darkColor,
                style = AppTypography.displayLarge.copy(fontSize = TextUnit(24f, TextUnitType.Sp))
            )

            Spacer(Modifier.width(8.dp))
            Text(
                text = "Russian",
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable { selectedLanguage = "Russian" },
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
