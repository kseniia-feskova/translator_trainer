package com.translator.app.ui.screens.lesson.dictation

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

fun NavController.navigateToDictationLesson(
    setId: String, navOptions: NavOptions? = null,
) {
    this.navigate(LeafScreen.DictationLesson(setId), navOptions)
}

fun NavGraphBuilder.dictationLessonScreen(
    navigateToSuccess: (LessonType) -> Unit = { _ -> },
    navigateUp: () -> Unit = {}
) {
    composable<LeafScreen.DictationLesson> {
        DictationLessonRoute(
            navigateToSuccess = navigateToSuccess,
            navigateUp = navigateUp
        )
    }
}

internal val SavedStateHandle.setId: String
    get() = toRoute<LeafScreen.DictationLesson>().setId

@Composable
fun DictationLessonRoute(
    navigateUp: () -> Unit = {},
    navigateToSuccess: (LessonType) -> Unit = { _ -> },
    viewModel: DictationLessonViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()
    val baseState = viewModel.baseState.collectAsState()
    if (state.value != null) {
        state.value?.run {
            DictationLessonScreen(
                state = this,
                baseState = baseState.value,
                onPauseClick = viewModel::onPauseClicked,
                reload = viewModel::reload,
                navigateToSuccess = { viewModel.navigateToSuccess(navigateToSuccess) },
                navigateUp = navigateUp,
                onValueChange = viewModel::onValueChange,
                onTranslationSend = viewModel::onTranslate,
                onSkip = viewModel::onSkip
            )
        }
    }
}