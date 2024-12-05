package com.presentation.ui.screens.set

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


@Serializable
class CardSetRoute(val setId: Int)

fun NavController.navigateToSet(
    setId: Int,
    navOptions: NavOptions? = null
) {
    this.navigate(CardSetRoute(setId), navOptions)
}

fun NavGraphBuilder.setScreen(
    navigateToLesson: () -> Unit = {},
    navigateToEdit: () -> Unit = {},
    navigateUp: () -> Unit = {},
) {
    composable<CardSetRoute> {
        CardSetRoute(
            navigateToLesson = navigateToLesson,
            navigateUp = navigateUp,
            navigateToEdit = navigateToEdit
        )
    }
}

internal val SavedStateHandle.setId: Int
    get() = toRoute<CardSetRoute>().setId

@Composable
fun CardSetRoute(
    navigateToLesson: () -> Unit = {},
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
        startCourse = navigateToLesson,
        navigateUp = navigateUp,
        navigateToEdit = navigateToEdit
    )
}

