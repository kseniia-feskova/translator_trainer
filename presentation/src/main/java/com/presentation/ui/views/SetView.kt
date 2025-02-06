package com.presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.model.Level
import com.presentation.model.SetOfCards
import com.presentation.test.mockListOfSets
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.darkColor
import com.presentation.ui.lightLilaColor
import java.util.UUID


@Composable
fun SetOfCardsView(set: SetOfCards, background: Color, onSetSelected: (UUID) -> Unit = {}) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(color = background, shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clickable { onSetSelected(set.id) }
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = set.title,
            style = AppTypography.titleLarge.copy(
                color = darkColor,
                fontSize = TextUnit(18f, TextUnitType.Sp)
            ),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = "Learned: ${set.words.filter { it.level == Level.KNOW }.size}/${set.words.size}",
            style = AppTypography.titleLarge.copy(
                color = darkColor,
                fontSize = TextUnit(18f, TextUnitType.Sp)
            ),
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}


@Composable
fun ListOfSetsView(
    modifier: Modifier = Modifier,
    listOfSets: List<SetOfCards>,
    onSetSelected: (UUID) -> Unit = {},
) {
    LazyColumn(modifier = modifier.clip(RoundedCornerShape(20.dp))) {
        itemsIndexed(listOfSets) { index, item ->
            SetOfCardsView(
                item,
                if (index % 2 == 0) Color.White else lightLilaColor.copy(alpha = 0.5f),
                onSetSelected
            )
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
