package presentation.ui.screens.sets

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.androidx.compose.koinViewModel
import presentation.model.LessonType
import presentation.navigation.LeafScreen

fun NavGraphBuilder.setsScreen(
    navigateToSelectedSet: (String, String) -> Unit,
    navigateToAllWordsSet: (String) -> Unit,
    navigateToHome: () -> Unit,
    createNewSet: () -> Unit,
    navigateToLesson: (String, LessonType) -> Unit = { _, _ -> },
) {
    composable(route = LeafScreen.Sets.route) {
        SetsRoute(
            navigateToSelectedSet,
            navigateToAllWordsSet,
            navigateToHome,
            createNewSet,
            navigateToLesson,
        )
    }
}

@Composable
fun SetsRoute(
    navigateToSelectedSet: (String, String) -> Unit,
    navigateToAllWordsSet: (String) -> Unit,
    navigateToHome: () -> Unit,
    createNewSet: () -> Unit,
    navigateToLesson: (String, LessonType) -> Unit = { _, _ -> },
    viewModel: SetsViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    SetsScreen(
        state = state.value,
        navigateToSelectedSet = { id, name ->
            if (viewModel.isAllWordsSelected(id)) {
                Log.e("SetsNavigation", "navigateToAllWordsSet")
                navigateToAllWordsSet(id)
            } else {
                navigateToSelectedSet(id, name)
            }
        },
        navigateToHome = navigateToHome,
        createNewSet = createNewSet,
        createRandomLesson = {
            viewModel.createRandomLesson(navigateToLesson)
        },
    )

}