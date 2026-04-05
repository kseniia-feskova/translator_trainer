package com.translator.app.ui.views

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.translator.app.ui.darkColor
import com.translator.app.ui.whiteColor
import com.translator.app.ui.AppTypography

@Composable
fun MarqueeText(text: String) {
    val repeatedText = text.repeat(10)

    Row(
        modifier = Modifier.basicMarquee(repeatDelayMillis = 1, velocity = 30.dp)
    ) {
        repeatedText.chunked(text.length).forEachIndexed { index, chunk ->
            val color = if (index % 2 == 0) darkColor else whiteColor
            Text(text = chunk, color = color, style = AppTypography.displayLarge)
        }
    }
}
