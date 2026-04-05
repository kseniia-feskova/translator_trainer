package com.translator.app.ui.screens.all

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.koin.androidx.compose.koinViewModel
import com.translator.app.navigation.LeafScreen


fun NavController.navigateToAllWords(
    setId: String,
    navOptions: NavOptions? = null
) {
    this.navigate(LeafScreen.AllWords(setId), navOptions)
}

fun NavGraphBuilder.allWordsScreen(
    navigateUp: () -> Unit = {}
) {

    composable<LeafScreen.AllWords> {
        AllWordsRoute(navigateUp)
    }
}

internal val SavedStateHandle.setId: String
    get() = toRoute<LeafScreen.AllWords>().setId

@Composable
fun AllWordsRoute(
    navigateUp: () -> Unit = {},
    viewModel: AllWordsViewModel = koinViewModel()
) {

    val state = viewModel.uiState.collectAsState()

    AllWordsScreen(
        state = state.value,
        searchQuery = { viewModel.handleIntent(AllWordsIntent.Search(it)) },
        onClearClick = { viewModel.handleIntent(AllWordsIntent.ClearSearch) },
        onFilterClick = { viewModel.handleIntent(AllWordsIntent.Filter) },
        onDelete = { viewModel.handleIntent(AllWordsIntent.Delete(it)) },
        onRevealed = { viewModel.handleIntent(AllWordsIntent.OnActionsRevealed(it)) },
        onCollapsed = { viewModel.handleIntent(AllWordsIntent.OnCollapsed(it)) },
        onBackPressed = navigateUp
    )
}