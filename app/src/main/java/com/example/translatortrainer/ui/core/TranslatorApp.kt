package com.example.translatortrainer.ui.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.translatortrainer.ui.screens.MainScreen
import com.example.translatortrainer.ui.translate.TranslatorViewModel
import com.example.translatortrainer.ui.translate.model.TranslatorIntent

@Composable
fun TranslatorApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main",
    ) {
        composable("main") {
            val viewModel: TranslatorViewModel = viewModel()
            val state by viewModel.uiState.collectAsState()

            MainScreen(
                state = state,
                onWordInput = { viewModel.handleIntent(TranslatorIntent.EnterText(it)) }
            )
        }
    }
}