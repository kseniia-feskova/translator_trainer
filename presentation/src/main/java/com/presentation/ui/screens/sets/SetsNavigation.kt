package com.presentation.ui.screens.sets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.presentation.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.setsScreen(
    navigateToSelectedSet: (Int) -> Unit,
    navigateToAllWordsSet: (Int) -> Unit,
    navigateToHome: () -> Unit,
    createNewSet: () -> Unit,
    createRandomLesson: () -> Unit,
) {
    composable(route = LeafScreen.Sets.route) {
        SetsRoute(
            navigateToSelectedSet,
            navigateToAllWordsSet,
            navigateToHome,
            createNewSet,
            createRandomLesson
        )
    }
}

@Composable
fun SetsRoute(
    navigateToSelectedSet: (Int) -> Unit,
    navigateToAllWordsSet: (Int) -> Unit,
    navigateToHome: () -> Unit,
    createNewSet: () -> Unit,
    createRandomLesson: () -> Unit,
    viewModel: SetsViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    SetsScreen(
        state = state.value,
        navigateToSelectedSet = {
            if (viewModel.isAllWordsSelected(it)) {
                navigateToAllWordsSet(it)
            } else {
                navigateToSelectedSet(it)
            }
        },
        selectSet = viewModel::selectSet,
        navigateToHome = navigateToHome,
        createNewSet = createNewSet,
        createRandomLesson = createRandomLesson
    )

}