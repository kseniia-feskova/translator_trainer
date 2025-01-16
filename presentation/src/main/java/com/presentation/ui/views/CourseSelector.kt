package com.presentation.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.R
import com.presentation.ui.AppTheme
import com.presentation.model.CoursePreview
import com.presentation.utils.Language

@Composable
fun CourseSelector(
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    course: CoursePreview
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onCheckedChange(!checked) },
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = "${course.originalLanguage.name} - ${course.translateLanguage.name}",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(course.originalFlag),
            contentDescription = "Flag One",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(course.translatedFlag),
            contentDescription = "Flag Two",
            modifier = Modifier.size(40.dp)
        )
    }
}

@Preview
@Composable
fun CourseSelectorPreview() {
    AppTheme {
        Surface {
            CourseSelector(
                course = CoursePreview(
                    originalFlag = R.drawable.russia,
                    translatedFlag = R.drawable.germany,
                    originalLanguage = Language.RUSSIAN,
                    translateLanguage = Language.GERMAN
                )
            )
        }
    }
}