package com.presentation.ui.core

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.presentation.navigation.BottomNavItem
import com.presentation.ui.screens.course.second_translate.CourseSelectSecondTranslateScreen
import com.presentation.ui.screens.course.translate.CourseSelectTranslateScreen
import com.presentation.ui.screens.home.homeScreen
import com.presentation.ui.screens.set.CardSetScreen
import com.presentation.utils.Course
import com.presentation.viewmodel.CardSetIntent
import com.presentation.viewmodel.CardSetViewModel
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

        composable(
            route = "set/{setId}",
            arguments = listOf(navArgument("setId") { type = NavType.IntType })
        ) { backStackEntry ->
            val setId = backStackEntry.arguments?.getInt("setId") ?: return@composable
            val viewModel: CardSetViewModel = koinViewModel(parameters = { parametersOf(setId) })
            val state by viewModel.uiState.collectAsState()
            Log.d(TAG, "Open CardSetScreen setId = $setId")

            CardSetScreen(
                state = state,
                addWordToKnow = { viewModel.handleIntent(CardSetIntent.AddWordToKnow(it)) },
                addWordToLearn = { viewModel.handleIntent(CardSetIntent.AddWordToLearn(it)) },
                resetCardSet = { viewModel.handleIntent(CardSetIntent.ResetCardSet) },
                startCourse = {
                    viewModel.handleIntent(CardSetIntent.StartSelected { _setId, course ->
                        Log.d(TAG, "Call navigate to course ${course.route}")
                        navController.navigate("${course.route}/$_setId") {
                            popUpTo("set/${setId}") { inclusive = true }
                        }
                    })
                }
            )
        }

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