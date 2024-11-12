package com.presentation.ui.screens.course.second_translate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.presentation.model.Level
import com.presentation.model.WordUI
import com.presentation.ui.primaryColor
import com.presentation.ui.screens.course.CourseFrameView
import com.presentation.viewmodel.courses.CourseData
import java.util.Date

@Composable
fun CourseSelectSecondTranslateScreen(
    state: CourseData.SelectSecondTranslationData,
    onSelectedOption: (String) -> Unit = {},
    onDoNotKnowClick: () -> Unit = {},
    onExitClick: () -> Unit = {},
    onFinishLevel: () -> Unit = {}
) {
    Box(modifier = Modifier.background(primaryColor)) {
        if (state.finish) {
            onFinishLevel()
        }
        CourseFrameView(
            state = state,
            doNotKnowClicked = onDoNotKnowClick,
            onExitClick = onExitClick
        ) {
            SelectSecondTranslationView(
                modifier = Modifier.align(Alignment.Center),
                state = state,
                checkSelected = onSelectedOption
            )
        }
    }
}

@Preview
@Composable
fun SelectPreview() {
    CourseSelectSecondTranslateScreen(
        state = CourseData.SelectSecondTranslationData(
            WordUI(0, "Text on Deutsch", "Текст", Level.LEARNING, date = Date()),
            null,
            listOf(
                "First word",
                "Second word",
                "Текст",
                "Too loong word for translate",
                "And the other one"
            ),
            selectedOption = "",
            currentWordIndex = 1
        )
    )
}
