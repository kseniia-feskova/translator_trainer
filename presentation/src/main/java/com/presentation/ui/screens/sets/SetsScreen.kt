package com.presentation.ui.screens.sets

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.presentation.navigation.BottomNavigationBar
import com.presentation.test.mockListOfSets
import com.presentation.ui.AppTheme
import com.presentation.ui.views.ActionButton
import com.presentation.ui.views.BasicTopView
import com.presentation.ui.views.ListOfSetsView

@Composable
fun SetsScreen(
    state: SetsUIState,
    createNewSet: () -> Unit = {},
    selectSet: (Int?) -> Unit = {},
    navigateToHome: () -> Unit = {},
    navigateToSelectedSet: (Int) -> Unit = {},
    createRandomLesson: () -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        BasicTopView(
            title = "Наборы карточек",
            rightIcon = Icons.Default.Add,
            onRightClick = { createNewSet() })

        if (state.sets.isEmpty()) {
            Text(
                text = "У Вас нет сохранённых слов",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            ListOfSetsView(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp),
                listOfSets = state.sets,
                selectedSetId = state.selectedSetId,
                onSetClicked = selectSet,
                onSetSelected = navigateToSelectedSet
            )
        }

        ActionButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 72.dp)
                .padding(horizontal = 12.dp),
            onClick = {
                if (state.sets.isEmpty()) {
                    navigateToHome()
                } else {
                    createRandomLesson()
                }
            }) {
            Text(
                text = if (state.sets.isEmpty()) "Начать перевод" else "Случайный урок",
                modifier = Modifier.padding(6.dp),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = TextUnit(
                        16f,
                        TextUnitType.Sp
                    )
                )
            )
        }
    }
}


@Preview
@Composable
fun SetsScreenEmptyPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("Preview", "paddings $paddings")
            SetsScreen(
                SetsUIState(emptyList())
            )
        }, bottomBar = {
            val context = LocalContext.current
            BottomNavigationBar(navController = NavController(context))
        }
        )
    }
}

@Preview
@Composable
fun SetsScreenPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("Preview", "paddings $paddings")
            SetsScreen(
                SetsUIState(mockListOfSets)
            )
        }, bottomBar = {
            val context = LocalContext.current
            BottomNavigationBar(navController = NavController(context))
        }
        )
    }
}