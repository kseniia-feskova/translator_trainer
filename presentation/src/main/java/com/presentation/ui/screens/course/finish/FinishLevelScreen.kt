package com.presentation.ui.screens.course.finish

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.ui.primaryColor
import com.presentation.ui.secondaryColor

@Composable
fun FinishLevelScreen() {
    Scaffold { padding ->
        Log.d("CardSetScreen", "padding = $padding")
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = primaryColor)
        ) {
            Box(modifier = Modifier.align(Alignment.TopCenter)) {
                Text(text = "Набор слов №1")

                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close",
                    Modifier
                        .align(Alignment.CenterEnd)
                        .height(32.dp)
                        .clickable { },
                    tint = secondaryColor
                )
            }
        }
    }

}


@Preview
@Composable
fun FinishLevelPreview() {
    FinishLevelScreen()
}


/*
* Set Name
* Course Level
* Score?
*
* Intent -> close
*           again
*           next
*
*
* */