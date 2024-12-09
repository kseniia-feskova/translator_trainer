package com.presentation.ui.screens.set

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.presentation.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel

fun NavController.navigateToSet(
    setId: Int,
    navOptions: NavOptions? = null
) {
    this.navigate(LeafScreen.Set(setId), navOptions)
}

fun NavGraphBuilder.setScreen(
    navigateToLesson: (Int) -> Unit = {},
    navigateToEdit: () -> Unit = {},
    navigateUp: () -> Unit = {},
) {
    composable<LeafScreen.Set> {
        CardSetRoute(
            navigateToLesson = navigateToLesson,
            navigateUp = navigateUp,
            navigateToEdit = navigateToEdit
        )
    }
}

internal val SavedStateHandle.setId: Int
    get() = toRoute<LeafScreen.Set>().setId

@Composable
fun CardSetRoute(
    navigateToLesson: (Int) -> Unit = {},
    navigateToEdit: () -> Unit = {},
    navigateUp: () -> Unit = {},
    viewModel: SetViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    SetScreen(
        state = state.value,
        addWordToKnow = { viewModel.handleIntent(CardSetIntent.AddWordToKnow(it)) },
        addWordToLearn = { viewModel.handleIntent(CardSetIntent.AddWordToLearn(it)) },
        resetCardSet = { viewModel.handleIntent(CardSetIntent.ResetCardSet) },
        startCourse = {
            navigateToLesson(viewModel.getSetId())
        },
        navigateUp = navigateUp,
        navigateToEdit = navigateToEdit
    )
}

