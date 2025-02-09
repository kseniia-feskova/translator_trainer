package com.presentation.ui.screens.sets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.presentation.R
import com.presentation.navigation.BottomNavigationBar
import com.presentation.test.mockListOfSets
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.darkColor
import com.presentation.ui.redDarkColor
import com.presentation.ui.screens.auth.CustomShadowButton
import com.presentation.ui.views.BackgroundDecorAnimated
import com.presentation.ui.views.BaseTopView
import com.presentation.ui.views.ListOfSetsView
import com.presentation.ui.views.Loader
import java.util.UUID

@Composable
fun SetsScreen(
    state: SetsUIState,
    createNewSet: () -> Unit = {},
    navigateToHome: () -> Unit = {},
    navigateToSelectedSet: (UUID, String) -> Unit = { id, name -> },
    createRandomLesson: () -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if (state.sets.isEmpty()) {
            BackgroundDecorAnimated()
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BaseTopView(
                title = stringResource(R.string.card_sets_title),
                rightIcon = Icons.Default.LibraryAdd,
                onRightClick = { createNewSet() }
            )

            if (state.error != null) {
                Text(
                    text = stringResource(state.error.msg),
                    style = AppTypography.titleSmall.copy(color = redDarkColor),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            if (state.sets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.empty_sets_description),
                        style = MaterialTheme.typography.titleLarge.copy(color = darkColor)
                    )
                }
            } else {
                ListOfSetsView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 72.dp)
                        .weight(1f)
                        .clip(shape = RoundedCornerShape(20.dp)), // Занимает всё свободное пространство между верхней и нижней частью
                    listOfSets = state.sets,
                    onSetSelected = navigateToSelectedSet
                )
            }
        }
        CustomShadowButton(
            text = if (state.sets.isEmpty()) stringResource(R.string.to_translator_btn)
            else stringResource(R.string.random_lesson_btn),
            onClick = {
                if (state.sets.isEmpty()) {
                    navigateToHome()
                } else {
                    createRandomLesson()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter)
        )

        if (state.loading) {
            Loader(
                modifier = Modifier
                    .width(80.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
fun SetsScreenEmptyPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Box(modifier = Modifier.padding(paddings)) {
                SetsScreen(
                    SetsUIState(emptyList())
                )
            }
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
            Box(modifier = Modifier.padding(paddings)) {
                SetsScreen(
                    SetsUIState(mockListOfSets)
                )
            }
        }, bottomBar = {
            val context = LocalContext.current
            BottomNavigationBar(navController = NavController(context))
        }
        )
    }
}