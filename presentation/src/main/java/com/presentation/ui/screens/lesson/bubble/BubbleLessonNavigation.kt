package com.presentation.ui.screens.lesson.bubble

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
import java.util.UUID

fun NavController.navigateToBubbleLesson(
    setId: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(LeafScreen.BubbleLesson(setId), navOptions)
}

fun NavGraphBuilder.bubbleLessonScreen(
    navigateUp: () -> Unit = {}
) {
    composable<LeafScreen.BubbleLesson> {
        BubbleLessonRoute(
            navigateUp = navigateUp
        )
    }
}

internal val SavedStateHandle.setId: UUID
    get() = UUID.fromString(toRoute<LeafScreen.BubbleLesson>().setId)


@Composable
fun BubbleLessonRoute(
    navigateUp: () -> Unit = {},
    viewModel: BubbleLessonViewModel = koinViewModel()
) {
    val state = viewModel.bubbles.collectAsState()
    val lessonComplete = viewModel.lessonComplete.collectAsState()
    val lessonFailed = viewModel.lessonFailed.collectAsState()
    val onPause = viewModel.onPause.collectAsState()
    val lives = viewModel.lives.collectAsState()
    BubbleLessonScreen(
        mBubbles = state.value,
        isLessonCompleted = lessonComplete.value,
        isLessonFailed = lessonFailed.value,
        lives = lives.value,
        isPaused = onPause.value,
        initializeBubbles = viewModel::initializeBubbles,
        onBubbleClick = viewModel::onBubbleClick,
        onPauseClick = viewModel::onPauseClicked,
        reload = viewModel::reload,
        navigateUp = navigateUp,
    )

}