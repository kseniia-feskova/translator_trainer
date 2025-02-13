package com.presentation.ui.screens.home

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.presentation.navigation.LeafScreen
import com.presentation.navigation.RootScreen
import org.koin.androidx.compose.koinViewModel

fun NavController.navigateToHome(
    navOptions: NavOptions? = null,
) {
    this.navigate(RootScreen.Home.route, navOptions)
}

fun NavGraphBuilder.homeScreen(
) {
    composable(route = LeafScreen.Home.route,
        enterTransition = { fadeIn(animationSpec = tween(1000)) },
        exitTransition = { fadeOut(animationSpec = tween(500)) }
    ) { HomeRoute() }
}

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    HomeScreen(
        state = state.value,
        onWordInput = { viewModel.handleIntent(HomeIntent.InputText(it)) },
        onEnterText = { viewModel.handleIntent(HomeIntent.EnterText) },
        onSaveClick = { viewModel.handleIntent(HomeIntent.SaveWord) },
        onLanguageChange = { viewModel.handleIntent(HomeIntent.ChangeLanguages(it)) }
    )

}