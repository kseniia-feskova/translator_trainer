package com.presentation.ui.screens.set

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.ui.accentColor
import com.presentation.ui.primaryColor
import com.presentation.ui.secondaryColor


@Composable
fun ProgressForSet(
    modifier: Modifier = Modifier,
    current: Int,
    all: Int
) {
    Box(
        modifier = Modifier
            .then(modifier)
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
fun ProgressForCourse(
    modifier: Modifier = Modifier,
    current: Int = 0,
    all: Int = 5
) {
    Box() {
        Box(
            modifier = Modifier.align(Alignment.Center)
                .then(modifier)
                .fillMaxWidth()
                .background(color = secondaryColor, shape = RoundedCornerShape(8.dp))
                .wrapContentHeight()
        ) {
            val width = (current.toFloat() / all)
            Spacer(
                modifier = Modifier
                    .height(14.dp)
                    .background(color = accentColor, shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth(width)
            )
        }
        DynamicRowExample(current, all)
    }
}

@Composable
fun DynamicRowExample(current: Int, all: Int) {
    // Рисуем строку с заданным количеством элементов
    DynamicRow(current, itemCount = all)

}

@Composable
fun DynamicRow(current: Int, itemCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(itemCount) { index ->
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        if (index < current) accentColor else secondaryColor,
                        shape = RoundedCornerShape(24.dp)
                    )
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun ProgressForSetPreview() {
    Column() {
        Box(modifier = Modifier.padding(16.dp)) {
            ProgressForSet(current = 16, all = 80)
        }

        Box(
            modifier = Modifier
                .background(primaryColor)
                .padding(16.dp)
        ) {
            ProgressForCourse(modifier = Modifier.align(Alignment.BottomCenter), 2, 4)

        }
    }
}