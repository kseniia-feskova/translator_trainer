package com.translator.app.ui.screens.splash

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.presentation.ui.screens.splash.SplashScreen
import com.translator.app.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.splashScreen(
    navController: NavController,
) {
    composable(route = LeafScreen.Splash.route) { SplashRoute(navController) }
}

@Composable
fun SplashRoute(
    navController: NavController,
    viewModel: SplashViewModel = koinViewModel()
) {

    SplashScreen(
        viewModel = viewModel,
        navController = navController
    )

}