package com.presentation.ui.core

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.presentation.ui.screens.course.second_translate.CourseSelectSecondTranslateScreen
import com.presentation.ui.screens.course.translate.CourseSelectTranslateScreen
import com.presentation.ui.screens.main.MainScreen
import com.presentation.ui.screens.main.translate.model.TranslatorIntent
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
fun TranslatorApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main",
    ) {
        composable("main") {
            val viewModel: com.presentation.viewmodel.TranslatorViewModel = koinViewModel()
            val state by viewModel.uiState.collectAsState()
            val setsOfAllCards by viewModel.setsOfAllCards.collectAsState()
            val setsOfNewCards by viewModel.setsOfNewCards.collectAsState()
            val currentSet by viewModel.currentSet.collectAsState()
            Log.d(TAG, "Open MainScreen")

            MainScreen(
                state = state,
                setsOfAllCards = setsOfAllCards,
                setsOfNewCards = setsOfNewCards,
                setsOfCurrentCards = currentSet,
                onWordInput = { viewModel.handleIntent(TranslatorIntent.InputingText(it)) },
                onDeckSelect = { setId -> navController.navigate("set/$setId") },
                onEnterText = { text, originalLanguage, resLanguage ->
                    viewModel.handleIntent(
                        TranslatorIntent.EnterText(text, originalLanguage, resLanguage)
                    )
                },
                onFinishGlow = { viewModel.handleIntent(TranslatorIntent.HideGlow) },
                onLanguageChange = { viewModel.handleIntent(TranslatorIntent.ChangeLanguages) },
                onSaveClick = { viewModel.handleIntent(TranslatorIntent.SaveWord) }
            )
        }

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
                    viewModel.handleIntent(CardSetIntent.StartSelected { setId, course ->
                        navController.navigate("${course.route}/$setId")
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