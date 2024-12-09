package com.presentation.ui.screens.lesson

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.R
import com.presentation.test.smallList
import com.presentation.ui.AppTheme
import com.presentation.ui.accentColorLight
import com.presentation.ui.views.BasicTopView
import com.presentation.ui.views.ChipSelector
import com.presentation.ui.views.Loader
import com.presentation.ui.views.ProgressForLesson
import com.presentation.utils.CardView

@Composable
fun TranslateLessonScreen(
    state: LessonUIState,
    onStartLesson: () -> Unit = {},
    onOptionSelected: (String) -> Unit = {},
    onDontKnowClicked: () -> Unit = {},
    onCloseClicked: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.complete) {
            LessonPreloadView(
                modifier = Modifier.align(Alignment.Center),
                description = "Поздравляю! Урок пройден",
                title = state.title,
                showStart = state.showStart,
                onStartLesson = onCloseClicked,
            )
        } else {
            if (state.preLoading) {
                LessonPreloadView(
                    modifier = Modifier.align(Alignment.Center),
                    description = state.description,
                    title = state.title,
                    showStart = state.showStart,
                    onStartLesson = onStartLesson,
                )
            } else {
                state.lessonData?.let { level ->
                    LessonStepView(
                        state.title,
                        state.description,
                        state.index,
                        level,
                        onOptionSelected,
                        onDontKnowClicked,
                        onCloseClicked
                    )
                }
            }
        }
    }
}


@Composable
fun LessonStepView(
    title: String,
    description: String,
    index: Int,
    lesson: Lesson,
    onOptionSelected: (String) -> Unit = {},
    onDontKnowClicked: () -> Unit = {},
    onCloseClicked: () -> Unit = {}
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val cardHeight = screenHeight * 0.25f
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            BasicTopView(
                title = title,
                rightIcon = Icons.Default.Close,
                onRightClick = { onCloseClicked() })
            ProgressForLesson(
                modifier = Modifier.padding(24.dp),
                all = lesson.options.size,
                current = index
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp)
                .padding(bottom = (cardHeight)),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = description,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleLarge,
                fontSize = TextUnit(22f, TextUnitType.Sp)
            )
            CardView(
                cardHeight = cardHeight,
                firstWordUIPreview = lesson.currentWord,
                secondWordUIPreview = ""
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 8.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ChipSelector(
                modifier = Modifier,
                options = lesson.options,
                selectedOption = lesson.selectedOption.toString(),
                onOptionSelected = { onOptionSelected(it) },
                correctAnswer = lesson.correctOption
            )

            Text(
                text = "Не знаю",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onDontKnowClicked() },
                style = MaterialTheme.typography.titleMedium
            )
        }

    }
}


@Composable
fun LessonPreloadView(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    showStart: Boolean = false,
    onStartLesson: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.headlineLarge)
        Text(description, style = MaterialTheme.typography.titleLarge)
        if (showStart) {
            Icon(
                painterResource(R.drawable.ic_start),
                contentDescription = "Start",
                tint = accentColorLight,
                modifier = Modifier.clickable { onStartLesson() }
            )
        } else {
            Loader(
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

private val listOfStates =
    listOf(
        LessonUIState(
            preLoading = true,
            title = "Задание №1",
            description = "Выберите перевод",
            index = 1
        ),
        LessonUIState(
            preLoading = true,
            title = "Задание №1",
            description = "Выберите перевод",
            index = 1,
            showStart = true
        ),

        LessonUIState(
            preLoading = false,
            title = "Задание №1",
            description = "Выберите перевод",
            index = 1,
            showStart = true,
            lessonData = Lesson.TranslateLesson(
                words = smallList.take(5).toSet(),
                selectedWord = null,
                correctWord = smallList.first(),
            )
        ),
        LessonUIState(
            preLoading = false,
            title = "Задание №1",
            description = "Выберите перевод",
            index = 2,
            showStart = true,
            lessonData = Lesson.TranslateLesson(
                words = smallList.take(5).toSet(),
                selectedWord = smallList[1],
                correctWord = smallList[1],
            )
        ),

        LessonUIState(
            preLoading = true,
            title = "Задание №2",
            description = "Выберите оригинал",
            index = 1
        ),
        LessonUIState(
            preLoading = true,
            title = "Задание №2",
            description = "Выберите оригинал",
            index = 1,
            showStart = true
        ),

        LessonUIState(
            preLoading = false,
            title = "Задание №2",
            description = "Выберите оригинал",
            index = 2,
            showStart = true,
            lessonData = Lesson.OriginLesson(
                words = smallList.take(5).toSet(),
                selectedWord = null,
                correctWord = smallList[1],
            )
        ),
    )

private class PreviewProvider : PreviewParameterProvider<LessonUIState> {
    override val values: Sequence<LessonUIState>
        get() = listOfStates.asSequence()
}

@Preview()
@Composable
fun TranslateViewPreview(@PreviewParameter(PreviewProvider::class) state: LessonUIState) {
    AppTheme {
        Surface {
            TranslateLessonScreen(
                state = state
            )
        }
    }
}
