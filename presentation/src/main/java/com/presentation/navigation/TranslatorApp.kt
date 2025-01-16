package com.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.presentation.ui.screens.account.accountScreen
import com.presentation.ui.screens.all.allWordsScreen
import com.presentation.ui.screens.all.navigateToAllWords
import com.presentation.ui.screens.home.homeScreen
import com.presentation.ui.screens.home.navigateToHome
import com.presentation.ui.screens.lesson.LessonType
import com.presentation.ui.screens.lesson.lessonScreen
import com.presentation.ui.screens.lesson.navigateToLesson
import com.presentation.ui.screens.newset.navigateToNewSet
import com.presentation.ui.screens.newset.newSetScreen
import com.presentation.ui.screens.set.navigateToSet
import com.presentation.ui.screens.set.setScreen
import com.presentation.ui.screens.sets.setsScreen
import com.presentation.ui.screens.start.loginScreen

@Composable
fun TranslatorApp(navController: NavHostController, isUserAuthorized: Boolean = false) {
    val startDestination = if (isUserAuthorized) RootScreen.Home.route else LeafScreen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        LoginNav(navController)
        HomeNav()
        SetsNav(navController)
        AccountNav()
    }
}

private fun NavGraphBuilder.LoginNav(navController: NavHostController) {
    loginScreen {
        navController.navigate(RootScreen.Home.route) {
            popUpTo(LeafScreen.Login.route) { inclusive = true }
        }
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

private fun NavGraphBuilder.SetsNav(navController: NavHostController) {
    navigation(
        startDestination = LeafScreen.Sets.route, // Это для экрана
        route = RootScreen.Sets.route            // Это для графа
    ) {

        setsScreen(
            navigateToSelectedSet = { navController.navigateToSet(it.toString()) },
            navigateToAllWordsSet = { navController.navigateToAllWords(it.toString()) },
            navigateToHome = navController::navigateToHome,
            createNewSet = { navController.navigateToNewSet() },
            createRandomLesson = {}
        )

        setScreen(
            navigateToLesson = { navController.navigateToLesson(it.toString(), LessonType.TRANSLATE) },
            navigateToEdit = {},
            navigateUp = { navController.navigateUp() },
        )

        lessonScreen {
            navController.navigateUp()
        }

        allWordsScreen {
            navController.navigateUp()
        }

        newSetScreen {
            navController.navigateUp()
        }
    }
}

private fun NavGraphBuilder.AccountNav() {
    navigation(
        startDestination = LeafScreen.Account.route, // Это для экрана
        route = RootScreen.Profile.route            // Это для графа
    ) {
        accountScreen()
    }
}