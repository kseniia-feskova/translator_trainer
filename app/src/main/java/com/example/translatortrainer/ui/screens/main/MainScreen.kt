package com.example.translatortrainer.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.data.mock.model.SetOfCards
import com.data.translate.Language
import com.example.translatortrainer.ui.primaryColor
import com.example.translatortrainer.ui.screens.main.bottom.ThreeBottomView
import com.example.translatortrainer.ui.screens.main.bottom.mockSetOfCard
import com.example.translatortrainer.ui.screens.main.top.TopView
import com.example.translatortrainer.ui.screens.main.translate.TranslateView
import com.example.translatortrainer.ui.screens.main.translate.model.TranslatorState

@Composable
fun MainScreen(
    state: TranslatorState = TranslatorState(inputText = "Katze"),
    setsOfAllCards: SetOfCards? = null,
    setsOfNewCards: SetOfCards? = null,
    setsOfCurrentCards: SetOfCards? = null,
    onWordInput: (String) -> Unit = {},
    onDeckSelect: (Int) -> Unit = {},
    onEnterText: (String, Language, Language) -> Unit = { text, originalLanguage, resLanguage -> },
    onFinishGlow: () -> Unit = {},
    onLanguageChange: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    var showTopView by remember { mutableStateOf(false) }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    //val componentHeight = screenHeight * 0.2f
    val donutSize = screenHeight * 0.7f * 0.2f
    val bottomHeight = screenHeight * 0.35f

    Scaffold() { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = primaryColor)
        ) {

            // Animated top view
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
                    .align(Alignment.Center)
                    .padding(bottom = 100.dp),
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
                ThreeBottomView(
                    modifier = Modifier
                        .height(bottomHeight),
                    setsOfAllCards,
                    setsOfNewCards,
                    setsOfCurrentCards,
                    onDeckSelect
                )
            }

        }
    }

    // Start animation when the screen is loaded
    LaunchedEffect(Unit) {
        showTopView = true
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}

@Preview
@Composable
fun MainScreenWithButtonPreview() {
    MainScreen(
        state = TranslatorState().copy(
            inputText = "Katze",
            translatedText = "Котик"
        ),
        setsOfAllCards = mockSetOfCard,
        setsOfNewCards = mockSetOfCard,
        setsOfCurrentCards = mockSetOfCard
    )
}
