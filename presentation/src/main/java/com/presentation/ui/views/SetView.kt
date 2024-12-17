package com.presentation.ui.views

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    secondColor: Color,
    isElevated: Boolean = false,
    countOfWords: Int? = null,
    onSetClicked: () -> Unit = {},
    onSetSelected: () -> Unit = {},
) {

    val offsetY by animateDpAsState(
        targetValue = if (isElevated) (-40).dp else 0.dp,
        label = "offsetY"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = offsetY)
            .then(modifier)
    ) {
        Box(
            modifier = Modifier.background(
                    color = color,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                )
                .clickable { onSetClicked() }
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .align(Alignment.Center)
            )
        }
        Box(
            modifier = Modifier
                .background(
                    color = color,
                    shape = RoundedCornerShape(topEnd = 24.dp)
                )
                .clickable { onSetSelected() }
                .fillMaxWidth()
                .height(160.dp)
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            if (countOfWords != null) {
                Text(
                    text = "$countOfWords слов",
                    modifier = Modifier
                        .background(
                            color = secondColor,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onPrimary)
                )
            }
        }
    }
}

@Composable
fun ListOfSetsView(
    modifier: Modifier = Modifier,
    listOfSets: List<SetOfCards>,
    selectedSetId: Int? = null,
    onSetSelected: (Int) -> Unit = {},
    onSetClicked: (Int?) -> Unit = {}
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
                isElevated = selectedSetId == set.id,
                color = if ((listOfSets.size - i) % 2 == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                secondColor = if ((listOfSets.size - i) % 2 == 0) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                countOfWords = set.setOfWords.size,
                onSetClicked = {
                    if (selectedSetId == set.id) {
                        onSetClicked(null)
                    } else {
                        onSetClicked(set.id)
                    }
                },
                onSetSelected = {
                    if (selectedSetId == set.id) {
                        set.id?.let { onSetSelected(it) }
                    }
                }
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
