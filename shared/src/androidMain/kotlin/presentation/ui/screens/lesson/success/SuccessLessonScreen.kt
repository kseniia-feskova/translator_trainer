package presentation.ui.screens.lesson.success

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import presentation.model.LessonType
import com.presentation.ui.AppTheme
import presentation.ui.AppTypography
import com.presentation.ui.bgColor
import presentation.ui.screens.auth.CustomShadowButton
import presentation.ui.views.BackgroundDecorAnimated
import presentation.ui.views.BaseTopView

@Composable
fun SuccessLessonScreen(
    lessonType: LessonType,
    wordCount: Int,
    onContinue: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = bgColor)
    ) {
        BaseTopView(
            title = lessonType.name,
        )

        BackgroundDecorAnimated()

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Lesson Completed!", style = AppTypography.displayLarge)
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "Congratulations! You updated $wordCount word!",
                style = AppTypography.titleSmall.copy(fontSize = TextUnit(18f, TextUnitType.Sp))
            )
            Spacer(modifier = Modifier.height(32.dp))
            CustomShadowButton(
                text = "Continue",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                onClick = { onContinue() }
            )
        }

    }
}

@Preview
@Composable
fun SuccessLessonScreenPreview() {
    AppTheme {
        Scaffold {
            Box(modifier = Modifier.padding(it)) {
                SuccessLessonScreen(
                    lessonType = LessonType.BUBBLE,
                    3
                )
            }
        }
    }
}