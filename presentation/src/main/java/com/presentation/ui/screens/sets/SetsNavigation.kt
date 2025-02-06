package com.presentation.ui.screens.sets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.presentation.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

fun NavGraphBuilder.setsScreen(
    navigateToSelectedSet: (UUID) -> Unit,
    navigateToAllWordsSet: (UUID) -> Unit,
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
            createRandomLesson,
        )
    }
}

@Composable
fun SetsRoute(
    navigateToSelectedSet: (UUID) -> Unit,
    navigateToAllWordsSet: (UUID) -> Unit,
    navigateToHome: () -> Unit,
    createNewSet: () -> Unit,
    createRandomLesson: () -> Unit,
    viewModel: SetsViewModel = koinViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.reload()
        }
    }

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
        navigateToHome = navigateToHome,
        createNewSet = createNewSet,
        createRandomLesson = createRandomLesson,
    )

}