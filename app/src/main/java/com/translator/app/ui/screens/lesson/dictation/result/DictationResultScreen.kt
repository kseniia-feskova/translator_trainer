package com.translator.app.ui.screens.lesson.dictation.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.translator.app.ui.AppTheme
import com.translator.app.ui.darkColor
import com.translator.app.ui.gradientBrush
import com.translator.app.ui.greenColor
import com.translator.app.ui.redDarkColor
import presentation.model.LessonType
import presentation.model.WordResult
import presentation.model.WordUI
import presentation.test.smallList
import com.translator.app.ui.AppTypography
import com.translator.app.ui.views.BaseTopView
import com.translator.app.ui.views.WordWithStatus
import com.translator.app.ui.views.buttons.CustomShadowButton

@Composable
fun DictationResultScreen(
    lessonType: LessonType,
    mapOfAnswers: List<WordResult>,
    onContinue: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Column() {
            BaseTopView(
                title = lessonType.btnName + " lesson",
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Lesson Completed!",
                style = AppTypography.displayLarge
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Your score: ${mapOfAnswers.filter { it.translation.equals(it.word.resText, ignoreCase = true) }.size}/${mapOfAnswers.size}",
                style = AppTypography.titleMedium
            )
            LazyColumn(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 72.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
            ) {
                items(mapOfAnswers.toList()) {
                    AnswerView(it.word, it.translation)
                }
            }
        }
        CustomShadowButton(
            text = "Continue",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            onClick = { onContinue() }
        )
    }
}

@Composable
fun AnswerView(wordUI: WordUI, translation: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            if (translation.equals(wordUI.resText, ignoreCase = true)) "Correct!" else "Failure!",
            style = AppTypography.titleLarge.copy(color = if (translation.equals(wordUI.resText, ignoreCase = true)) greenColor else redDarkColor)
        )
        Text(
            "Word: ${wordUI.originalText}", style = AppTypography.titleMedium.copy(darkColor)
        )
        Text(
            "Correct answer: ${wordUI.resText}", style = AppTypography.titleMedium.copy(darkColor)
        )
        Text(
            "Your answer: $translation", style = AppTypography.titleMedium.copy(darkColor)
        )
    }
}

@Composable
fun TranslatedWord(wordUI: WordUI, translation: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Your translation: ",
                style = AppTypography.titleMedium.copy(darkColor)
            )
            Text(
                translation,
                style = AppTypography.titleLarge.copy(color = if (translation.equals(wordUI.resText, ignoreCase = true)) greenColor else redDarkColor)
            )
        }
        WordWithStatus(modifier = Modifier.padding(bottom = 8.dp), wordUI)
    }
}

@Preview
@Composable
fun DictationResultScreenPreview() {
    AppTheme {
        Scaffold {
            Box(modifier = Modifier.padding(it)) {
                DictationResultScreen(
                    lessonType = LessonType.DICTATION,
                    mapOfAnswers = listOf(
                        WordResult(smallList.first(), "Test"),
                        WordResult(smallList[1], "Котик"),
                        WordResult(smallList[2], "мама"),
                        WordResult(smallList[2], "Gfgf")
                    )
                )
            }
        }
    }
}