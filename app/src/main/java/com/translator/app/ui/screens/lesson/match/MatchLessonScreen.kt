package com.translator.app.ui.screens.lesson.match

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.translator.app.ui.AppTheme
import com.translator.app.ui.screens.lesson.base.BaseLessonScreen
import com.translator.app.ui.screens.lesson.base.BaseLessonState
import com.translator.app.ui.screens.lesson.match.model.MatchViewData
import com.translator.app.ui.screens.lesson.match.model.testOriginalData
import com.translator.app.ui.screens.lesson.match.model.testTranslationData
import com.translator.app.ui.screens.lesson.match.view.MatchWordView

@Composable
fun MatchLessonScreen(
    state: MatchLessonState,
    baseState: BaseLessonState,
    onPauseClick: () -> Unit = {},
    reload: () -> Unit = {},
    navigateUp: () -> Unit = {},
    navigateToSuccess: () -> Unit = { },
    onWordClicked: (MatchViewData) -> Unit = {}
) {
    BaseLessonScreen(
        baseState,
        onPauseClick,
        reload,
        navigateUp,
        navigateToSuccess
    ) {
        GridWithButtons(state.leftColumn, state.rightColumn, onWordClicked)
    }
}

@Composable
fun GridWithButtons(
    leftColumn: List<MatchViewData>,
    rightColumn: List<MatchViewData>,
    onWordClicked: (MatchViewData) -> Unit = {}
) {
    Row(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = leftColumn, key = { it.id }) { word ->
                MatchWordView(
                    word = word,
                    modifier = Modifier,
                    onWordClicked = onWordClicked
                )
            }
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = rightColumn, key = { it.id }) { word ->
                MatchWordView(
                    word = word,
                    modifier = Modifier,
                    onWordClicked = onWordClicked
                )
            }
        }
    }
}

@Preview
@Composable
fun MatchLessonScreenPreview() {
    AppTheme {
        Scaffold {
            Box(modifier = Modifier.padding(it)) {
                MatchLessonScreen(
                    MatchLessonState(testTranslationData, testOriginalData),
                    BaseLessonState(
                        isLessonCompleted = false,
                        isLessonFailed = false,
                        isPaused = false,
                        lives = 3,
                        time = 60L
                    )
                )
            }
        }
    }
}