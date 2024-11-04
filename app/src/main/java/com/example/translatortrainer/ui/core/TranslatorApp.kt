package com.example.translatortrainer.ui.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            val setsOfCards by viewModel.setsOfCards.collectAsState()

            MainScreen(
                state = state,
                setsOfCards = setsOfCards,
                onWordInput = { viewModel.handleIntent(TranslatorIntent.InputingText(it)) },
                onDeckSelect = { navController.navigate("set") },
                onEnterText = { text, originalLanguage, resLanguage -> viewModel.handleIntent(TranslatorIntent.EnterText(text, originalLanguage, resLanguage)) },
                onFinishGlow = { viewModel.handleIntent(TranslatorIntent.HideGlow) },
                onLanguageChange = {viewModel.handleIntent(TranslatorIntent.ChangeLanguages)},
                onSaveClick = {viewModel.handleIntent(TranslatorIntent.SaveWord)}
            )
        }

        composable("set") {
            val viewModel: CardSetViewModel = koinViewModel()
            val state by viewModel.uiState.collectAsState()

            CardSetScreen(
                state = state,
                addWordToKnow = { viewModel.handleIntent(CardSetIntent.AddWordToKnow(it)) },
                addWordToLearn = { viewModel.handleIntent(CardSetIntent.AddWordToLearn(it)) },
                resetCardSet = { viewModel.handleIntent(CardSetIntent.ResetCardSet) },
                startCourse = {
                    viewModel.handleIntent(CardSetIntent.SaveSelected)
                    navController.navigate("course")
                }
            )
        }

        composable("course") {
            val viewModel: SelectTranslateViewModel = koinViewModel()
            val state by viewModel.uiState.collectAsState()

            CourseSelectTranslateScreen(
                state = state,
                onSelectedOption = {
                    viewModel.handleIntent(SelectTranslationIntent.SelectTranslation(it))
                },
                onDoNotKnowClick = { viewModel.handleIntent(SelectTranslationIntent.DoNotKnow) },
                onExitClick = { navController.navigateUp() }
            )
        }
    }
}