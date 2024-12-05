package com.presentation.ui.screens.set

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.model.Level
import com.presentation.model.WordUI
import com.presentation.test.mockSetOfCard
import com.presentation.ui.accentColor
import com.presentation.ui.primaryColor
import com.presentation.ui.secondaryColor
import com.presentation.ui.views.ActionButton
import com.presentation.ui.views.BasicTopView
import com.presentation.utils.ALL_WORDS

private val mockAllCardsState = AllCardsState(
    allWords = mockSetOfCard.setOfWords.size,
    title = mockSetOfCard.title,
    knowWords = mockSetOfCard.setOfWords.toList().filter { it.level == Level.KNOW }.size,
    words = mockSetOfCard.setOfWords
)

@Composable
fun AllCardsScreen(
    state: AllCardsState = AllCardsState()

) {
    Scaffold(
        topBar = {
            BasicTopView(title = ALL_WORDS)
        },
        bottomBar = {
            if (state.words.isNotEmpty()) {
                ActionButton(modifier = Modifier
                    .padding(24.dp),
                    onClick = { }) {
                    Text(modifier = Modifier.padding(6.dp), text = "Изучить набор")
                }
            }
        }
    ) { innerPadding ->
        Log.e("Padding", "innerPadding = $innerPadding")
        Box(
            modifier = Modifier
                .background(primaryColor)
                .padding(top = innerPadding.calculateTopPadding() + 16.dp)
                .padding(bottom = innerPadding.calculateBottomPadding() + 16.dp)
                .padding(start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + 16.dp)
                .padding(end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) + 16.dp)
                .fillMaxSize()
        ) {
            if (state.words.isNotEmpty()) {
                ItemList(
                    items = state.words.toList()
                )
            } else {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 56.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "У вас пока нет новых слов\nВы можете добавить их из готовых наборов или воспользоваться нашим переводчиком",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = TextUnit(16f, TextUnitType.Sp),
                        color = secondaryColor,
                        textAlign = TextAlign.Center
                    )

                    ActionButton(modifier = Modifier.padding(24.dp), onClick = { }) {
                        Text(
                            text = "Перейти на главную", modifier = Modifier.padding(6.dp)
                        )
                    }
                }
            }
        }
    }
}

data class AllCardsState(
    val knowWords: Int = 0,
    val allWords: Int = 20,
    val title: String = "All cards",
    val words: Set<WordUI> = emptySet()
)


@Composable
fun ItemList(modifier: Modifier = Modifier, items: List<WordUI>) {
    LazyColumn(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            ListItem(item)
        }
    }
}

@Composable
fun ListItem(item: WordUI) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = secondaryColor,
                shape = RoundedCornerShape(16.dp)
            ),
    ) {
        Text(
            text = item.originalText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextUnit(22f, TextUnitType.Sp),
            color = primaryColor,
        )

        Text(
            text = item.resText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp, bottom = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontSize = TextUnit(16f, TextUnitType.Sp),
            color = primaryColor,
        )

        StarsRow(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            item.level.convertToStars()
        )

    }
}

@Composable
fun StarsRow(modifier: Modifier = Modifier, items: List<Boolean>) {
    LazyRow(
        modifier = Modifier.then(modifier),
    ) {
        items(items) { item ->
            StarIcon(item)
        }
    }
}

@Composable
fun StarIcon(isColorful: Boolean) {

    Icon(
        imageVector = Icons.Default.Star,
        contentDescription = "Star",
        tint = if (isColorful) primaryColor else accentColor
    )

}

@Preview
@Composable
fun AllCardsPreview() {
    AllCardsScreen(mockAllCardsState)
}


@Preview
@Composable
fun AllCardsEmptyPreview() {
    AllCardsScreen()
}