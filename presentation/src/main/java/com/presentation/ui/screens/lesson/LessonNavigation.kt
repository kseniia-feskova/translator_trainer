package com.presentation.ui.screens.lesson

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.presentation.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel

fun NavController.navigateToLesson(
    setId: Int,
    lessonType: LessonType,
    navOptions: NavOptions? = null,
) {
    this.navigate(LeafScreen.Lesson(setId, lessonType), navOptions)
}

fun NavGraphBuilder.lessonScreen(
    navigateUp: () -> Unit = {}
) {
    composable<LeafScreen.Lesson> {
        LessonRoute(
            navigateUp = navigateUp
        )
    }
}

internal val SavedStateHandle.setId: Int
    get() = toRoute<LeafScreen.Lesson>().setId


internal val SavedStateHandle.type: LessonType
    get() = toRoute<LeafScreen.Lesson>().type


@Composable
fun LessonRoute(
    navigateUp: () -> Unit = {},
    viewModel: LessonViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    TranslateLessonScreen(
        state = state.value,
        onStartLesson = { viewModel.handleIntent(LessonIntent.Start) },
        onOptionSelected = { viewModel.handleIntent(LessonIntent.OptionSelected(it)) },
        onDontKnowClicked = { viewModel.handleIntent(LessonIntent.DontKnow) },
        onCloseClicked = navigateUp
    )

}