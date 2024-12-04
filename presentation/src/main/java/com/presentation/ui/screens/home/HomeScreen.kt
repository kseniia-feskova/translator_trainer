package com.presentation.ui.screens.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.presentation.ui.views.Loader
import com.presentation.ui.views.HomeTopView
import com.presentation.ui.views.TranslateView

@Composable
fun HomeScreen(
    state: HomeUIState,
    onWordInput: (String) -> Unit = {},
    onEnterText: (String) -> Unit = { },
    onSaveClick: () -> Unit = {},
    onLanguageChange: () -> Unit = {}
) {
    var showTopView by remember { mutableStateOf(false) }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val donutSize = screenHeight * 0.17f

    LaunchedEffect(Unit) { showTopView = true }

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
            HomeTopView(18, donutSize = donutSize)
        }
        Spacer(modifier = Modifier.height(100.dp))

        TranslateView(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.Center),
            state = state,
            onTextChange = onWordInput,
            onEnterText = onEnterText,
            onSaveClick = onSaveClick,
            onLanguageChange = onLanguageChange
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
            HomeScreen(
                state = HomeUIState().copy(
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
