package com.presentation.ui.screens.select_course


import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.model.CourseUI
import com.presentation.test.dummyCourses
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.bgColor
import com.presentation.ui.fieldBorderColor
import com.presentation.ui.lightLilaColor
import com.presentation.ui.screens.auth.CustomShadowButton
import com.presentation.ui.views.BackgroundDecorAnimated
import com.presentation.ui.views.CustomRadioButton
import com.presentation.ui.whiteColor

@Composable
fun SelectCourseScreen(
    state: SelectCourseUIState,
    onCourseSelected: (CourseUI) -> Unit = {},
    onContinueClicked: () -> Unit = {},
    onBackClicked: () -> Unit = {}
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        BackgroundDecorAnimated()
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .basicMarquee(
                    repeatDelayMillis = 1,
                    velocity = 30.dp,
                    iterations = 0
                )
                .padding(horizontal = 18.dp, vertical = 24.dp)
        ) {
            Text(text = "Before we start", color = whiteColor, style = AppTypography.displayLarge)
        }
        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(screenHeight * 0.6f)
                .background(
                    whiteColor,
                    shape = RoundedCornerShape(topEnd = 36.dp, topStart = 36.dp)
                )
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Text(
                "Select course",
                style = AppTypography.displayLarge.copy(fontSize = TextUnit(22f, TextUnitType.Sp)),
                color = bgColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )


            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxHeight()) {
                LazyColumn {
                    items(state.courses) { course ->
                        CourseItem(
                            course = course,
                            isSelected = state.selectedCourse == course,
                            onSelect = { onCourseSelected(it) }
                        )
                    }
                }

                Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                    Text(
                        "Back",
                        modifier = Modifier.align(Alignment.CenterHorizontally).clickable { onBackClicked() },
                        color = bgColor,
                        style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    CustomShadowButton("Continue", onClick = onContinueClicked)
                }
            }
        }
    }
}


@Composable
fun CourseItem(course: CourseUI, isSelected: Boolean, onSelect: (CourseUI) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { onSelect(course) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomRadioButton(
            modifier = Modifier.size(32.dp),
            selected = isSelected,
            onClick = { onSelect(course) },
            unselectedColor = lightLilaColor,
            selectedColor = bgColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${course.originalLanguage} - ${course.translateLanguage}",
            color = bgColor,
            style = AppTypography.titleSmall.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .height(32.dp)
                .border(width = 1.dp, color = fieldBorderColor, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            painter = painterResource(course.originalFlag),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            modifier = Modifier
                .height(32.dp)
                .border(width = 1.dp, color = fieldBorderColor, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            painter = painterResource(course.translatedFlag),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}


@Preview
@Composable
fun SelectCourseScreenPreview() {
    AppTheme {
        Surface {
            SelectCourseScreen(
                SelectCourseUIState(
                    courses = dummyCourses,
                    selectedCourse = dummyCourses.firstOrNull()
                )
            )
        }
    }
}

