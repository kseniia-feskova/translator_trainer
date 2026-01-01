package presentation.ui.screens.newset

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import presentation.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel


fun NavController.navigateToNewSet(
    navOptions: NavOptions? = null
) {
    this.navigate(LeafScreen.NewSet.route, navOptions)
}

fun NavGraphBuilder.newSetScreen(
    navigateUp: () -> Unit = {},
    goToAccount: () -> Unit
) {

    composable(LeafScreen.NewSet.route) {
        NewSetRoute(navigateUp, goToAccount)
    }
}

@Composable
fun NewSetRoute(
    navigateUp: () -> Unit = {},
    goToAccount: () -> Unit = {},
    viewModel: NewSetViewModel = koinViewModel()
) {

    val state = viewModel.uiState.collectAsState()

    NewSetScreen(
        state = state.value,
        onNameChange = { viewModel.handleIntent(NewSetIntent.NameChange(it)) },
        onSaveCheckBoxChange = { viewModel.handleIntent(NewSetIntent.SaveCheckBoxChange(it)) },
        selectWord = { viewModel.handleIntent(NewSetIntent.SelectWord(it)) },
        saveSet = { viewModel.handleIntent(NewSetIntent.SaveSet(navigateUp)) },
        navigateUp = navigateUp,
        goToAccount = goToAccount,
        hideLimitsError = { viewModel.handleIntent(NewSetIntent.HideLimitsError) },
        sortedBy = { viewModel.handleIntent(NewSetIntent.FilterClicked) }
    )
}