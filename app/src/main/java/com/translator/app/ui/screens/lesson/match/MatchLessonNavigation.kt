package com.translator.app.ui.screens.lesson.match

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.koin.androidx.compose.koinViewModel
import presentation.model.LessonType
import com.translator.app.navigation.LeafScreen

fun NavController.navigateToMatchLesson(
    setId: String, navOptions: NavOptions? = null,
) {
    this.navigate(LeafScreen.MatchLesson(setId), navOptions)
}

fun NavGraphBuilder.matchLessonScreen(
    navigateToSuccess: (LessonType, Int) -> Unit = { _, _ -> },
    navigateUp: () -> Unit = {}
) {
    composable<LeafScreen.MatchLesson> {
        MatchLessonRoute(
            navigateToSuccess = navigateToSuccess,
            navigateUp = navigateUp
        )
    }
}

internal val SavedStateHandle.setId: String
    get() = toRoute<LeafScreen.MatchLesson>().setId

@Composable
fun MatchLessonRoute(
    navigateUp: () -> Unit = {},
    navigateToSuccess: (LessonType, Int) -> Unit = { _, _ -> },
    viewModel: MatchLessonViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()
    val baseState = viewModel.baseState.collectAsState()
    if (state.value != null) {
        state.value?.run {
            MatchLessonScreen(
                state = this,
                baseState = baseState.value,
                onPauseClick = viewModel::onPauseClicked,
                reload = viewModel::reload,
                navigateToSuccess = { viewModel.navigateToSuccess(navigateToSuccess) },
                navigateUp = navigateUp,
                onWordClicked = viewModel::onWordSelected
            )
        }
    }
}