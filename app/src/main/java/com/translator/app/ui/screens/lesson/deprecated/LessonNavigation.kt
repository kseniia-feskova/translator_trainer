package com.translator.app.ui.screens.lesson.deprecated
/*
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import domain.model.LessonType
import presentation.navigation.LeafScreen

fun NavController.navigateToLesson(
    setId: String,
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

internal val SavedStateHandle.setId: UUID
    get() = UUID.fromString(toRoute<LeafScreen.Lesson>().setId)


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

}*/