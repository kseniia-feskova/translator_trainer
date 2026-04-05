package com.translator.app.ui.screens.lesson.dictation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.translatortrainer.shared.R
import com.translator.app.ui.AppTheme
import com.translator.app.ui.darkColor
import com.translator.app.ui.lightLilaColor
import presentation.model.WordUI
import presentation.test.smallList
import com.translator.app.ui.screens.lesson.base.BaseLessonScreen
import com.translator.app.ui.screens.lesson.base.BaseLessonState
import com.translator.app.ui.views.buttons.CustomShadowButton
import com.translator.app.ui.views.input.OutlinedTextField

data class DictationLessonState(
    val currentWord: WordUI,
    val inputText: String = "",
    val countOfWords: Int = 10,
    val currentWordNumber: Int = 0
)

@Composable
fun DictationLessonScreen(
    state: DictationLessonState,
    baseState: BaseLessonState,
    onPauseClick: () -> Unit = {},
    reload: () -> Unit = {},
    navigateUp: () -> Unit = {},
    navigateToSuccess: () -> Unit = { },
    onValueChange: (String) -> Unit = {},
    onTranslationSend: () -> Unit = {},
    onSkip: () -> Unit = {},
) {
    BaseLessonScreen(
        baseState,
        onPauseClick,
        reload,
        navigateUp,
        navigateToSuccess
    ) {
        DictationView(state, onValueChange, onTranslationSend, onSkip)
    }
}

@Composable
fun DictationView(
    state: DictationLessonState,
    onValueChange: (String) -> Unit = {},
    onTranslationSend: () -> Unit = {},
    onSkip: () -> Unit = {},
) {

    Column(modifier = Modifier.padding(32.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(200.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(24.dp),
                    color = lightLilaColor
                )
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = state.currentWord.originalText,
                style = MaterialTheme.typography.displayLarge,
                color = darkColor,
                textAlign = TextAlign.Center
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp),
            text = state.currentWordNumber.toString() + "/" + state.countOfWords.toString(),
            style = MaterialTheme.typography.titleSmall,
            color = darkColor
        )


        OutlinedTextField(
            value = state.inputText,
            label = stringResource(R.string.dictation_input_label),
            onValueChange = onValueChange
        )

        CustomShadowButton(
            modifier = Modifier.padding(top = 32.dp),
            isEnabled = state.inputText.isNotEmpty(),
            text = stringResource(R.string.dictation_send_button_label),
            onClick = onTranslationSend
        )
        Text(
            modifier = Modifier
                .clickable { onSkip() }
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            text = "Skip",
            style = MaterialTheme.typography.titleSmall,
            color = darkColor
        )

    }

}

@Preview
@Composable
fun DictationLessonScreenPreview() {
    AppTheme {
        Scaffold {
            Box(modifier = Modifier.padding(it)) {
                DictationLessonScreen(
                    DictationLessonState(smallList.last()),
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