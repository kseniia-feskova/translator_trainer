package com.presentation.ui.screens.course.translate

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
import java.util.UUID

@Composable
fun CourseSelectTranslateScreen(
    state: CourseData.SelectTranslationData,
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
            SelectTranslationView(
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
    CourseSelectTranslateScreen(
        state = CourseData.SelectTranslationData(
            WordUI(UUID.fromString("0"), "Text on Deutsch", "Текст", Level.KNOW),
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
