package com.presentation.ui.screens.set

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.presentation.model.WordUI
import com.presentation.navigation.BottomNavigationBar
import com.presentation.test.mockListOfSets
import com.presentation.ui.AppTheme
import com.presentation.ui.accentColorLight
import com.presentation.ui.views.ActionButton
import com.presentation.ui.views.BasicTopView
import com.presentation.ui.views.CardsSet
import com.presentation.ui.views.ProgressForSet

@Composable
fun SetScreen(
    state: SetUIState,
    addWordToKnow: (WordUI) -> Unit = {},
    addWordToLearn: (WordUI) -> Unit = {},
    resetCardSet: () -> Unit = {},
    startCourse: () -> Unit = {},
    navigateToEdit: () -> Unit = {},
    navigateUp: () -> Unit = {}
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val cardHeight = screenHeight * 0.25f

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column() {
            BasicTopView(
                title = state.name,
                rightIcon = Icons.Default.Edit,
                leftIcon = Icons.Default.ArrowBackIosNew,
                onRightClick = navigateToEdit,
                onLeftClick = navigateUp
            )

            ProgressForSet(
                modifier = Modifier.padding(12.dp),
                current = state.knowWords,
                all = state.allWords
            )
        }

        if (state.words != null) {
            CardsSet(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
                cardHeight = cardHeight,
                state.words.first,
                state.words.second,
                onRightSwipe =  {
                    addWordToKnow(it)

                } ,
                onLeftSwipe = {
                    addWordToLearn(it)

                }
            )

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(top = cardHeight * 1.4f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Left",
                        tint = accentColorLight
                    )
                    Text(
                        text = "Не знаю",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = TextUnit(16f, TextUnitType.Sp)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally,) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Right",
                        tint = accentColorLight
                    )
                    Text(
                        text = "Знаю",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = TextUnit(16f, TextUnitType.Sp),
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .zIndex(2f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Вы отсортировали все слова",
                    style = MaterialTheme.typography.titleMedium,
                )
                ActionButton(onClick = { resetCardSet() }) {
                    Text(
                        text = "Отсортировать заново",
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
            }
        }

        ActionButton(
            onClick = { startCourse() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 12.dp)
                .padding(bottom = 72.dp)
        ) {
            Text(
                text = "Изучить набор",
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000999)
@Composable
fun CardSetScreenPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("CardSetScreenPreview", "paddings = $paddings")
            SetScreen(
                state = SetUIState(
                    words = Pair(
                        mockListOfSets[2].setOfWords.last(),
                        mockListOfSets[2].setOfWords.first()
                    )
                )
            )
        }, bottomBar = {
            BottomNavigationBar(navController = NavController(LocalContext.current))
        })
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000999)
@Composable
fun EmptyCardSetScreenPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("CardSetScreenPreview", "paddings = $paddings")
            SetScreen(
                state = SetUIState(
                    words = null
                )
            )
        }, bottomBar = {
            BottomNavigationBar(navController = NavController(LocalContext.current))
        })
    }
}