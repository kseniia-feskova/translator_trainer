package com.example.translatortrainer.ui.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.translatortrainer.ui.screens.course.translate.CourseSelectTranslateScreen
import com.example.translatortrainer.ui.screens.main.MainScreen
import com.example.translatortrainer.ui.screens.main.translate.model.TranslatorIntent
import com.example.translatortrainer.ui.screens.set.CardSetScreen
import com.example.translatortrainer.viewmodel.CardSetIntent
import com.example.translatortrainer.viewmodel.CardSetViewModel
import com.example.translatortrainer.viewmodel.TranslatorViewModel
import com.example.translatortrainer.viewmodel.courses.SelectTranslateViewModel
import com.example.translatortrainer.viewmodel.courses.SelectTranslationIntent
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TranslatorApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main",
    ) {
        composable("main") {
            val viewModel: TranslatorViewModel = koinViewModel()
            val state by viewModel.uiState.collectAsState()
            val setsOfAllCards by viewModel.setsOfAllCards.collectAsState()
            val setsOfNewCards by viewModel.setsOfNewCards.collectAsState()
            val currentSet by viewModel.currentSet.collectAsState()

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

            CardSetScreen(
                state = state,
                addWordToKnow = { viewModel.handleIntent(CardSetIntent.AddWordToKnow(it)) },
                addWordToLearn = { viewModel.handleIntent(CardSetIntent.AddWordToLearn(it)) },
                resetCardSet = { viewModel.handleIntent(CardSetIntent.ResetCardSet) },
                startCourse = {
                    viewModel.handleIntent(CardSetIntent.StartSelected { setId ->
                        navController.navigate("course/$setId")
                    })
                }
            )
        }

        composable(
            route = "course/{setId}",
            arguments = listOf(navArgument("setId") { type = NavType.IntType })
        ) { backStackEntry ->
            val setId = backStackEntry.arguments?.getInt("setId") ?: return@composable
            val viewModel: SelectTranslateViewModel =
                koinViewModel(parameters = { parametersOf(setId) })
            val state by viewModel.uiState.collectAsState()

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
    }
}