package presentation.ui.screens.home

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import presentation.navigation.LeafScreen
import presentation.navigation.RootScreen
import org.koin.androidx.compose.koinViewModel

fun NavController.navigateToHome(
    navOptions: NavOptions? = null,
) {
    this.navigate(RootScreen.Home.route, navOptions)
}

fun NavGraphBuilder.homeScreen(
    goToAccount: () -> Unit
) {
    composable(route = LeafScreen.Home.route,
        enterTransition = { fadeIn(animationSpec = tween(1000)) },
        exitTransition = { fadeOut(animationSpec = tween(500)) }
    ) {
        HomeRoute(goToAccount = goToAccount)
    }
}

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = koinViewModel(),
    goToAccount: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()
    HomeScreen(
        state = state.value,
        goToAccount = goToAccount,
        hideLimitsError = {viewModel.handleIntent(HomeIntent.HideLimitsError)},
        onWordInput = { viewModel.handleIntent(HomeIntent.InputText(it)) },
        onEnterText = { viewModel.handleIntent(HomeIntent.EnterText) },
        onSaveClick = { viewModel.handleIntent(HomeIntent.SaveWord) },
        onLanguageChange = { viewModel.handleIntent(HomeIntent.ChangeLanguages(it)) }
    )

}