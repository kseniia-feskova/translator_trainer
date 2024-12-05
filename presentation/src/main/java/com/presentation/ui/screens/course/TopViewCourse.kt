package com.presentation.ui.screens.course

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.ui.primaryColor
import com.presentation.ui.secondaryColor
import com.presentation.ui.views.ProgressForLesson

@Composable
fun TopViewCourse(
    modifier: Modifier = Modifier,
    currentWord: Int = 0,
    allWords: Int = 5,
    name: String,
    onExitClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
    ) {
        TopCourseTitle(onExitClick = onExitClick, name = name)
        Spacer(modifier = Modifier.height(16.dp))
        ProgressForLesson(current = currentWord, all = allWords)
        Spacer(modifier = Modifier.height(16.dp))
    }

}

@Composable
fun TopCourseTitle(
    modifier: Modifier = Modifier, name: String = "Набор слов №1", onExitClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(top = 8.dp),
    ) {

        Text(
            text = name,
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextUnit(22f, TextUnitType.Sp),
            color = secondaryColor,
        )

        Icon(
            Icons.Default.Close,
            contentDescription = "Close",
            Modifier
                .align(Alignment.CenterEnd)
                .height(32.dp)
                .clickable { onExitClick() },
            tint = secondaryColor
        )
    }
}

@Composable
@Preview
fun TopViewCoursePreview() {
    Box(modifier = Modifier.background(primaryColor)) {
        TopViewCourse(modifier = Modifier.padding(8.dp), 1, 5, name = "Все слова")
    }
}