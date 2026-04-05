package com.translator.app.ui.screens.lesson.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.translator.app.ui.AppTheme
import com.translator.app.ui.AppTypography
import com.translator.app.ui.screens.lesson.bubble.OnFailScreen
import com.translator.app.ui.screens.lesson.bubble.OnPauseScreen
import com.translator.app.ui.views.LessonTopView
import com.translator.app.utils.toTimeFormat

data class BaseLessonState(
    val time: Long = 60L,
    val isLessonCompleted: Boolean = false,
    val isLessonFailed: Boolean = false,
    val isPaused: Boolean = false,
    val lives: Int = 3
)

@Composable
fun BaseLessonScreen(
    baseState: BaseLessonState,
    onPauseClick: () -> Unit = {},
    reload: () -> Unit = {},
    navigateUp: () -> Unit = {},
    navigateToSuccess: () -> Unit = { },
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        Modifier.fillMaxSize()
    ) {
        if (baseState.isLessonCompleted) {
            navigateToSuccess()
        }
        Column {
            LessonTopView(baseState.lives) { onPauseClick() }
            Box(content = content)
        }
        if (baseState.isPaused) {
            OnPauseScreen(onContinue = { onPauseClick() }, navigateUp)
        }

        if (baseState.isLessonFailed) {
            OnFailScreen(tryAgain = { reload() }, onCloseLesson = navigateUp)
        }
        if (!baseState.isPaused && !baseState.isLessonFailed) {
            if (baseState.time > 0L) {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(32.dp),
                    text = baseState.time.toTimeFormat(),
                    style = AppTypography.titleMedium
                )
            } else {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(32.dp),
                    text = "Time is over",
                    style = AppTypography.titleMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun BaseLessonPreview() {
    AppTheme {
        Scaffold {
            Box(modifier = Modifier.padding(it)) {
                BaseLessonScreen(
                    BaseLessonState(
                        isLessonCompleted = false,
                        isLessonFailed = false,
                        isPaused = false,
                        lives = 3,
                        time = 60L
                    )
                ) {}
            }
        }
    }
}