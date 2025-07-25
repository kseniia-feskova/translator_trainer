package presentation.ui.screens.lesson.success

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.koin.androidx.compose.koinViewModel
import presentation.model.LessonType
import presentation.navigation.LeafScreen

fun NavController.navigateToSuccessLesson(
    wordsCount: Int,
    type: LessonType,
    navOptions: NavOptions? = null,
) {
    this.navigate(LeafScreen.SuccessLesson(type, wordsCount), navOptions)
}

fun NavGraphBuilder.successLessonScreen(
    navigateToSet: () -> Unit = {}
) {
    composable<LeafScreen.SuccessLesson> {
        SuccessLessonRoute(
            navigateToSet = navigateToSet
        )
    }
}

internal val SavedStateHandle.wordsCount: Int
    get() = toRoute<LeafScreen.SuccessLesson>().wordsCount

internal val SavedStateHandle.type: LessonType
    get() = toRoute<LeafScreen.SuccessLesson>().type


@Composable
fun SuccessLessonRoute(
    viewModel: SuccessLessonViewModel = koinViewModel(),
    navigateToSet: () -> Unit = {},
) {
    SuccessLessonScreen(
        wordCount = viewModel.wordsCount,
        lessonType = viewModel.type,
        onContinue = { navigateToSet() }
    )
}