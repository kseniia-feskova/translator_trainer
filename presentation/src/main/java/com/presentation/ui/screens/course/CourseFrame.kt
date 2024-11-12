package com.presentation.ui.screens.course

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.model.Level
import com.presentation.model.WordUI
import com.presentation.ui.primaryColor
import com.presentation.ui.screens.course.translate.SelectTranslationView
import com.presentation.ui.secondaryColor

@Composable
fun CourseFrameView(
    state: com.presentation.viewmodel.courses.CourseData,
    doNotKnowClicked: () -> Unit = {},
    onExitClick: () -> Unit = {},
    content: @Composable() (BoxScope.() -> Unit),
) {
    Box(Modifier.fillMaxSize()) {
        TopViewCourse(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopCenter),
            currentWord = state.currentWordIndex,
            allWords = state.allWordsCount,
            onExitClick = onExitClick,
            name = state.setName
        )

        content()

        Text(
            modifier = Modifier
                .clickable { doNotKnowClicked() }
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            text = "Не знаю", style = MaterialTheme.typography.bodyLarge,
            color = secondaryColor,
            fontSize = TextUnit(18f, TextUnitType.Sp)
        )
    }

}

@Composable
@Preview
fun CourseFrameViewPreview() {
    Box(modifier = Modifier.background(primaryColor)) {
        CourseFrameView(
            com.presentation.viewmodel.courses.CourseData.SelectTranslationData(
                WordUI(0, "Text", "Текст", Level.KNOW),
                null,
                emptyList(),
                selectedOption = "",
                currentWordIndex = 1
            )
        ) {
            SelectTranslationView(
                modifier = Modifier.align(Alignment.Center),
                state = com.presentation.viewmodel.courses.CourseData.SelectTranslationData(
                    WordUI(0, "Text on Deutsch", "Текст", com.presentation.model.Level.KNOW),
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
    }
}
