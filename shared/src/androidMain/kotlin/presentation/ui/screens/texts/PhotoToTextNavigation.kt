package com.presentation.ui.screens.texts

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import presentation.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel
import presentation.ui.screens.texts.TextFromPhotoViewModel
import presentation.ui.screens.texts.TextRecognitionScreen

fun NavGraphBuilder.photoToText(
) {
    composable(route = LeafScreen.PhotoToText.route,
        enterTransition = { fadeIn(animationSpec = tween(1000)) },
        exitTransition = { fadeOut(animationSpec = tween(500)) }
    ) { PhotoToTextRoute() }
}

@Composable
fun PhotoToTextRoute(
    viewModel: TextFromPhotoViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    TextRecognitionScreen(
        state = state.value,
        onSelect = viewModel::onSelect,
        onSaveClick = viewModel::saveWord,
        onLanguageChange = viewModel::changeLanguages,
        recognizeText = viewModel::recognizeText
    )
}