package com.presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.model.SetOfCards
import com.presentation.test.mockListOfSets
import com.presentation.ui.AppTheme

@Composable
fun SetView(
    modifier: Modifier = Modifier,
    title: String,
    color: Color,
    countOfWords: Int? = null,
    onSetSelected: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
            .clickable { onSetSelected() }
    ) {
        Box(
            modifier = Modifier.background(
                color = color,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .align(Alignment.Center)
            )
        }
        Box(
            modifier = Modifier
                .background(
                    color = color,
                    shape = RoundedCornerShape(topEnd = 24.dp)
                )
                .fillMaxWidth()
                .height(160.dp)
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            if (countOfWords != null) {
                Text(
                    text = "$countOfWords слов",
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun ListOfSetsView(
    modifier: Modifier = Modifier,
    listOfSets: List<SetOfCards>,
    onSetSelected: (Int) -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        var i = 0
        while (i < listOfSets.size) {
            val set = listOfSets[i]
            SetView(
                modifier = Modifier.padding(top = (56 * (i + 1)).dp),
                title = set.title,
                color = if ((listOfSets.size - i) % 2 == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                countOfWords = if (i == listOfSets.size - 1) set.setOfWords.size else null,
                onSetSelected = { set.id?.let { onSetSelected(it) } }
            )
            i++
        }
    }
}

@Preview
@Composable
fun SetViewPreview() {
    AppTheme {
        Surface {
            ListOfSetsView(listOfSets = mockListOfSets)
        }
    }
}
