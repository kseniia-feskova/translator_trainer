package com.presentation.ui.screens.main

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.presentation.navigation.BottomNavigationBar
import com.presentation.ui.AppTheme
import com.presentation.ui.screens.main.top.TopView
import com.presentation.ui.screens.main.translate.TranslateView
import com.presentation.ui.screens.main.translate.model.TranslatorState
import com.presentation.utils.Language

@Composable
fun HomeScreen(
    state: TranslatorState = TranslatorState(inputText = "Katze"),
    onWordInput: (String) -> Unit = {},
    onEnterText: (String, Language, Language) -> Unit = { _, _, _ -> },
    onFinishGlow: () -> Unit = {},
    onLanguageChange: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    var showTopView by remember { mutableStateOf(false) }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val donutSize = screenHeight * 0.17f
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

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
            TopView(18, donutSize = donutSize)
        }

        Spacer(modifier = Modifier.height(100.dp))

        TranslateView(
            modifier = Modifier
                .align(Alignment.Center),
            state = state,
            onTextChange = onWordInput,
            onEnterText = onEnterText,
            onFinishGlow = onFinishGlow,
            onLanguageChange = onLanguageChange,
            onSaveClick = onSaveClick
        )

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            visible = showTopView,
            enter = fadeIn(tween(1000)) +
                    slideInVertically(
                        initialOffsetY = { it } // Начальная позиция - вне экрана (снизу)
                    ),
            exit = fadeOut(tween(500)) +
                    slideOutVertically(
                        targetOffsetY = { it } // Конечная позиция - вне экрана (снизу)
                    )
        ) {

        }
    }

    LaunchedEffect(Unit) {
        showTopView = true
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    AppTheme {
        Surface {
            HomeScreen()
        }
    }
}

@Preview
@Composable
fun MainScreenWithButtonPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("Preview", "paddings $paddings")
            HomeScreen(
                state = TranslatorState().copy(
                    inputText = "Katze",
                    translatedText = "Котик"
                )
            )
        }, bottomBar = {
            val context = LocalContext.current
            BottomNavigationBar(navController = NavController(context))
        }
        )
    }
}
