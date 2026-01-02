package presentation.ui.screens.lesson.bubble

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import presentation.model.LessonType
import presentation.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel

fun NavController.navigateToBubbleLesson(
    setId: String, navOptions: NavOptions? = null,
) {
    this.navigate(LeafScreen.BubbleLesson(setId), navOptions)
}

fun NavGraphBuilder.bubbleLessonScreen(
    navigateToSuccess: (LessonType, Int) -> Unit = { _, _ -> },
    navigateUp: () -> Unit = {}
) {
    composable<LeafScreen.BubbleLesson> {
        BubbleLessonRoute(
            navigateToSuccess = navigateToSuccess,
            navigateUp = navigateUp
        )
    }
}

internal val SavedStateHandle.setId: String
    get() = toRoute<LeafScreen.BubbleLesson>().setId


@Composable
fun BubbleLessonRoute(
    navigateUp: () -> Unit = {},
    navigateToSuccess: (LessonType, Int) -> Unit = { _, _ -> },
    viewModel: BubbleLessonViewModel = koinViewModel()
) {
    val state = viewModel.bubbles.collectAsState()
    val baseState = viewModel.baseState.collectAsState()
    BubbleLessonScreen(
        mBubbles = state.value,
        baseState = baseState.value,
        initializeBubbles = viewModel::initializeBubbles,
        onBubbleClick = viewModel::onBubbleClick,
        onPauseClick = viewModel::onPauseClicked,
        reload = viewModel::reload,
        navigateToSuccess = { viewModel.navigateToSuccess(navigateToSuccess) },
        navigateUp = navigateUp,
    )
}