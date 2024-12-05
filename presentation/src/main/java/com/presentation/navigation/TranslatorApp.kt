package com.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.presentation.ui.screens.home.homeScreen
import com.presentation.ui.screens.home.navigateToHome
import com.presentation.ui.screens.set.navigateToSet
import com.presentation.ui.screens.set.setScreen
import com.presentation.ui.screens.sets.setsScreen

@Composable
fun TranslatorApp(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = RootScreen.Home.route
    ) {
        HomeNav()
        SetsNav(navController)
    }
}

private fun NavGraphBuilder.HomeNav() {
    navigation(
        startDestination = LeafScreen.Home.route,
        route = RootScreen.Home.route
    ) {
        homeScreen()
    }
}


fun NavGraphBuilder.SetsNav(navController: NavHostController) {
    navigation(
        startDestination = LeafScreen.Sets.route, // Это для экрана
        route = RootScreen.Sets.route            // Это для графа
    ) {

        setsScreen(
            navigateToSelectedSet = { navController.navigateToSet(it) },
            navigateToHome = navController::navigateToHome,
            createNewSet = {},
            createRandomLesson = {}
        )

        setScreen(
            navigateToLesson = {},
            navigateToEdit = {},
            navigateUp = { navController.navigateUp() },
        )
    }
}