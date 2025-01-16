package com.presentation.ui.screens.all

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
import java.util.UUID


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

internal val SavedStateHandle.setId: UUID
    get() = UUID.fromString(toRoute<LeafScreen.AllWords>().setId)

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
        onWordSelected = { word, offset ->
            viewModel.handleIntent(
                AllWordsIntent.WordSelected(
                    word,
                    offset
                )
            )
        },
        onDismissRequest = { viewModel.handleIntent(AllWordsIntent.CloseDialog) },
        onEdit = { viewModel.handleIntent(AllWordsIntent.Edit(it)) },
        onDelete = { viewModel.handleIntent(AllWordsIntent.Delete(it)) },
        onBackPressed = navigateUp
    )
}