package presentation.ui.screens.sets

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import presentation.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel
import presentation.model.LessonType

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
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.reload()
        }
    }

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