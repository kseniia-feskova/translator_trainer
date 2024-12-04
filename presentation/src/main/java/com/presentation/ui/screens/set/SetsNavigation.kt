package com.presentation.ui.screens.set

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.presentation.navigation.BottomNavItem
import org.koin.androidx.compose.koinViewModel

const val setsScreen = "sets_screen"

fun NavGraphBuilder.setsScreen(
    navigateToSelectedSet: (String) -> Unit,
    navigateToHome: () -> Unit,
    createNewSet: () -> Unit,
    createRandomLesson: () -> Unit,
) {
    composable(BottomNavItem.Sets.route) {
        SetsRoute(
            navigateToSelectedSet,
            navigateToHome,
            createNewSet,
            createRandomLesson
        )
    }
}

@Composable
fun SetsRoute(
    navigateToSelectedSet: (String) -> Unit,
    navigateToHome: () -> Unit,
    createNewSet: () -> Unit,
    createRandomLesson: () -> Unit,
    viewModel: SetsViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    SetsScreen(
        state = state.value,
        navigateToSelectedSet = { navigateToSelectedSet(it.id.toString()) },
        navigateToHome = navigateToHome,
        createNewSet = createNewSet,
        createRandomLesson = createRandomLesson
    )

}