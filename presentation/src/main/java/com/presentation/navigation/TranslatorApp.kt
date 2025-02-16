package com.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.presentation.model.LessonType
import com.presentation.ui.screens.account.accountScreen
import com.presentation.ui.screens.all.allWordsScreen
import com.presentation.ui.screens.all.navigateToAllWords
import com.presentation.ui.screens.auth.authScreen
import com.presentation.ui.screens.auth.navigateToAuth
import com.presentation.ui.screens.home.homeScreen
import com.presentation.ui.screens.home.navigateToHome
import com.presentation.ui.screens.lesson.bubble.bubbleLessonScreen
import com.presentation.ui.screens.lesson.bubble.navigateToBubbleLesson
import com.presentation.ui.screens.lesson.success.navigateToSuccessLesson
import com.presentation.ui.screens.lesson.success.successLessonScreen
import com.presentation.ui.screens.newset.navigateToNewSet
import com.presentation.ui.screens.newset.newSetScreen
import com.presentation.ui.screens.select_course.selectCourseScreen
import com.presentation.ui.screens.set.navigateToSet
import com.presentation.ui.screens.set.setScreen
import com.presentation.ui.screens.sets.setsScreen
import com.presentation.ui.screens.splash.splashScreen

@Composable
fun TranslatorApp(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = LeafScreen.Splash.route
    ) {
        splashScreen(navController)
        loginNav(navController)
        homeNav()
        setsNav(navController)
        accountNav()
    }
}

private fun NavGraphBuilder.loginNav(navController: NavHostController) {
    authScreen(
        goToCourses = {
            navController.navigate(LeafScreen.SelectCourse.route) {
                popUpTo(LeafScreen.Login.route) { inclusive = true }
            }
        },
        goToHome = {
            navController.navigate(RootScreen.Home.route) {
                popUpTo(LeafScreen.Login.route) { inclusive = true }
            }
        }
    )
    selectCourseScreen(
        navigateUp = {
            navController.navigateToAuth(
                NavOptions.Builder()
                    .setPopUpTo(
                        navController.graph.startDestinationId,
                        inclusive = true
                    )
                    .setLaunchSingleTop(true)
                    .build()
            )
        },
        goToHome = {
            navController.navigate(RootScreen.Home.route) {
                popUpTo(LeafScreen.Login.route) { inclusive = true }
            }
        }
    )
}

private fun NavGraphBuilder.homeNav() {
    navigation(
        startDestination = LeafScreen.Home.route,
        route = RootScreen.Home.route
    ) {
        homeScreen()
    }
}

private fun NavGraphBuilder.setsNav(navController: NavHostController) {
    navigation(
        startDestination = LeafScreen.Sets.route, // Это для экрана
        route = RootScreen.Sets.route            // Это для графа
    ) {

        setsScreen(
            navigateToSelectedSet = { id, name ->
                navController.navigateToSet(
                    id.toString(),
                    name
                )
            },
            navigateToAllWordsSet = { navController.navigateToAllWords(it.toString()) },
            navigateToHome = navController::navigateToHome,
            createNewSet = { navController.navigateToNewSet() },
            createRandomLesson = {}
        )

        setScreen(
            navigateToLesson = { id, type ->
                when (type) {
                    LessonType.BUBBLE -> navController.navigateToBubbleLesson(id.toString())
                    else -> navController.navigateToBubbleLesson(id.toString())
                }
            },
            navigateToEdit = {},
            navigateUp = { navController.navigateUp() },
        )


        successLessonScreen {
            navController.navigate(LeafScreen.Sets.route) {
                popUpTo(LeafScreen.Sets.route) { inclusive = true }
                launchSingleTop = true
            }
        }

        bubbleLessonScreen(
            navigateToSuccess = { type, words ->
                navController.navigateToSuccessLesson(words, type)
            },
            navigateUp = {
                navController.navigateUp()
            })

        allWordsScreen {
            navController.navigateUp()
        }

        newSetScreen {
            navController.navigateUp()
        }
    }
}

private fun NavGraphBuilder.accountNav() {
    navigation(
        startDestination = LeafScreen.Account.route, // Это для экрана
        route = RootScreen.Profile.route            // Это для графа
    ) {
        accountScreen()
    }
}