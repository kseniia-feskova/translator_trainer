package com.example.translatortrainer.ui.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.translatortrainer.ui.screens.main.MainScreen
import com.example.translatortrainer.ui.screens.main.translate.model.TranslatorIntent
import com.example.translatortrainer.ui.screens.set.CardSetScreen
import com.example.translatortrainer.viewmodel.CardSetViewModel
import com.example.translatortrainer.viewmodel.TranslatorViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TranslatorApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main",
    ) {
        composable("main") {
            val viewModel: TranslatorViewModel = koinViewModel()//<TranslatorViewModel>()
            val state by viewModel.uiState.collectAsState()

            MainScreen(
                state = state,
                onWordInput = { viewModel.handleIntent(TranslatorIntent.EnterText(it)) },
                onDeckSelect = { navController.navigate("set") }
            )
        }

        composable("set") {
            val viewModel: CardSetViewModel = koinViewModel()//<CardSetViewModel>()
            val state by viewModel.uiState.collectAsState()

            CardSetScreen(
                state = state,
                addWordToKnow = {},
                addWordToLearn = {},
                startCourse = {}
            )
        }
    }
}