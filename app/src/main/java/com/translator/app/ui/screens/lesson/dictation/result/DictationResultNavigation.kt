package com.translator.app.ui.screens.lesson.dictation.result

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.koin.androidx.compose.koinViewModel
import presentation.model.LessonType
import com.translator.app.navigation.LeafScreen

fun NavController.navigateToDictationResult(
    type: LessonType,
    navOptions: NavOptions? = null,
) {
    this.navigate(LeafScreen.DictationResult(type), navOptions)
}

fun NavGraphBuilder.dictationResultScreen(
    navigateToSet: () -> Unit = {}
) {
    composable<LeafScreen.DictationResult> {
        DictationResultRoute(
            navigateToSet = navigateToSet
        )
    }
}

internal val SavedStateHandle.dictationType: LessonType
    get() = toRoute<LeafScreen.DictationResult>().type

@Composable
fun DictationResultRoute(
    viewModel: DictationResultViewModel = koinViewModel(),
    navigateToSet: () -> Unit = {},
) {
    DictationResultScreen(
        mapOfAnswers = viewModel.answers,
        lessonType = viewModel.type,
        onContinue = {
            viewModel.onContinue()
            navigateToSet()
        }
    )
}