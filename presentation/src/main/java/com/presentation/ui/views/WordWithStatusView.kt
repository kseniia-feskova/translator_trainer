package com.presentation.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.model.WordUI
import com.presentation.test.smallList
import com.presentation.ui.AppTheme
import com.presentation.ui.screens.all.StarsRow


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordWithStatusView(
    modifier: Modifier = Modifier,
    word: WordUI,
    onLongClicked: (WordUI, Offset) -> Unit = { _, _ -> }
) {
    var popupOffset by remember { mutableStateOf<Offset?>(null) }
    Box(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .onGloballyPositioned { layoutCoordinates ->
                val bounds = layoutCoordinates.boundsInRoot()
                popupOffset = Offset(bounds.left, bounds.bottom)
            }
            .combinedClickable(
                onClick = {  },
                onLongClick = { popupOffset?.let { onLongClicked(word, it) } }
            )
            .clipToBounds()
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Text(text = word.originalText, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = word.resText, style = MaterialTheme.typography.titleSmall)
        }

        StarsRow(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(12.dp),
            stars = word.level.convertToStars()
        )


    }
}

@Composable
@Preview
fun WordWithStatusPreview() {
    AppTheme {
        Surface {
            WordWithStatusView(modifier = Modifier.padding(12.dp), word = smallList.first())
        }
    }
}