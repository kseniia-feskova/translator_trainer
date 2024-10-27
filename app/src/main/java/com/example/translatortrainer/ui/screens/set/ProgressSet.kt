package com.example.translatortrainer.ui.screens.set

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.translatortrainer.ui.accentColor
import com.example.translatortrainer.ui.primaryColor

@Composable
fun ProgressForSet(
    modifier: Modifier = Modifier,
    current: Int,
    all: Int
) {
    Box(
        modifier = Modifier.then(modifier)
            .fillMaxWidth()
            .background(color = accentColor, shape = RoundedCornerShape(8.dp))
            .height(14.dp)
    ) {
        val width = (current.toFloat() / all)
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .background(color = primaryColor, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth(width)
        )

    }

}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun ProgressForSetPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        ProgressForSet(current = 16, all = 80)
    }
}