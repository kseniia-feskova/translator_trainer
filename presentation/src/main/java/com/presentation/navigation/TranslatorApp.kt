package com.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.presentation.ui.screens.course.second_translate.CourseSelectSecondTranslateScreen
import com.presentation.ui.screens.course.translate.CourseSelectTranslateScreen
import com.presentation.ui.screens.home.homeScreen
import com.presentation.ui.screens.home.navigateToHome
import com.presentation.ui.screens.set.navigateToSet
import com.presentation.ui.screens.set.setScreen
import com.presentation.ui.screens.sets.setsScreen
import com.presentation.utils.Course
import com.presentation.viewmodel.courses.SelectSecondTranslateViewModel
import com.presentation.viewmodel.courses.SelectSecondTranslationIntent
import com.presentation.viewmodel.courses.SelectTranslateViewModel
import com.presentation.viewmodel.courses.SelectTranslationIntent
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private const val TAG = "TranslatorApp"

@Composable
fun TranslatorApp(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
    ) {
        homeScreen()
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

        composable(
            route = "${Course.SELECT_RUSSIAN.route}/{setId}",
            arguments = listOf(navArgument("setId") { type = NavType.IntType })
        ) { backStackEntry ->
            val setId = backStackEntry.arguments?.getInt("setId") ?: return@composable
            val viewModel: SelectTranslateViewModel =
                koinViewModel(parameters = { parametersOf(setId, true) })
            val state by viewModel.uiState.collectAsState()
            Log.d(TAG, "First course for setId = $setId")

            CourseSelectTranslateScreen(
                state = state,
                onSelectedOption = {
                    viewModel.handleIntent(SelectTranslationIntent.SelectTranslation(it))
                },
                onDoNotKnowClick = { viewModel.handleIntent(SelectTranslationIntent.DoNotKnow) },
                onExitClick = { navController.navigateUp() },
                onFinishLevel = { navController.navigateUp() }
            )
        }

        composable(
            route = "${Course.SELECT_DEUTSCH.route}/{setId}",
            arguments = listOf(navArgument("setId") { type = NavType.IntType })
        ) { backStackEntry ->
            val setId = backStackEntry.arguments?.getInt("setId") ?: return@composable
            val viewModel: SelectSecondTranslateViewModel =
                koinViewModel(parameters = { parametersOf(setId, false) })
            val state by viewModel.uiState.collectAsState()

            Log.d(TAG, "Second course for setId = $setId")

            CourseSelectSecondTranslateScreen(
                state = state,
                onSelectedOption = {
                    viewModel.handleIntent(SelectSecondTranslationIntent.SelectTranslation(it))
                },
                onDoNotKnowClick = { viewModel.handleIntent(SelectSecondTranslationIntent.DoNotKnow) },
                onExitClick = { navController.navigateUp() },
                onFinishLevel = { navController.navigateUp() }
            )
        }
    }
}