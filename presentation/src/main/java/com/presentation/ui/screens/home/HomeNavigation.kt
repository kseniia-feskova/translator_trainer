package com.presentation.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.presentation.navigation.BottomNavItem
import org.koin.androidx.compose.koinViewModel

const val homeScreen = "home_screen"

fun NavGraphBuilder.homeScreen(
) {
    composable(BottomNavItem.Home.route) { HomeRoute() }
}

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    HomeScreen(
        state = state.value,
        onWordInput = { viewModel.handleIntent(HomeIntent.InputText(it)) },
        onEnterText = { viewModel.handleIntent(HomeIntent.EnterText(it)) },
        onSaveClick = { viewModel.handleIntent(HomeIntent.SaveWord) },
        onLanguageChange = {viewModel.handleIntent(HomeIntent.ChangeLanguages)}
    )

}