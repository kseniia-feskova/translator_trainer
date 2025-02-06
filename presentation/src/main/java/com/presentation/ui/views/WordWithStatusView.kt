package com.presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.model.WordUI
import com.presentation.test.smallList
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.bgColor
import com.presentation.ui.lightLilaColor
import com.presentation.ui.screens.all.StarsRow

//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun WordWithStatusView(
//    modifier: Modifier = Modifier,
//    word: WordUI,
//) {
//    Box(
//        modifier = Modifier
//            .then(modifier)
//            .fillMaxWidth()
//            .background(
//                color = MaterialTheme.colorScheme.primary,
//                shape = RoundedCornerShape(12.dp)
//            )
//            .clipToBounds()
//    ) {
//
//        Column(
//            modifier = Modifier
//                .align(Alignment.CenterStart)
//                .padding(horizontal = 12.dp, vertical = 16.dp)
//        ) {
//            Text(text = word.originalText, style = MaterialTheme.typography.titleLarge)
//            Spacer(modifier = Modifier.height(6.dp))
//            Text(text = word.resText, style = MaterialTheme.typography.titleSmall)
//        }
//
//        StarsRow(
//            modifier = Modifier
//                .align(Alignment.CenterEnd)
//                .padding(12.dp),
//            stars = word.level.convertToStars()
//        )
//
//
//    }
//}

@Composable
fun SelectingWordWithStatusView(
    modifier: Modifier = Modifier,
    word: WordUI,
    isSelected: Boolean,
    onClick: (WordUI) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        CustomRadioButton(
            modifier = Modifier.size(32.dp),
            selected = isSelected,
            onClick = { onClick(word) },
            unselectedColor = lightLilaColor,
            selectedColor = bgColor
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onClick(word) })
        ) {

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 12.dp, vertical = 20.dp)
            ) {
                Text(
                    text = word.originalText,
                    style = AppTypography.titleLarge.copy(color = bgColor)
                )
                Text(text = word.resText, style = AppTypography.titleSmall.copy(color = bgColor))
            }

            StarsRow(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(12.dp),
                stars = word.level.convertToStars()
            )
        }
    }
}


@Composable
@Preview
fun WordWithStatusPreview() {
    AppTheme {
        Surface {
            Column {
                //WordWithStatusView(modifier = Modifier.padding(12.dp), word = smallList.first())

                SelectingWordWithStatusView(
                    modifier = Modifier.padding(12.dp),
                    word = smallList.first(),
                    isSelected = false
                )

                SelectingWordWithStatusView(
                    modifier = Modifier.padding(12.dp),
                    word = smallList.first(),
                    isSelected = true
                )
            }
        }
    }
}