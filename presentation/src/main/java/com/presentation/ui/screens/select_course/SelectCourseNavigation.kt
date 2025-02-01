package com.presentation.ui.screens.select_course

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.presentation.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel

fun NavController.navigateToSelectCourse(
    navOptions: NavOptions? = null
) {
    this.navigate(LeafScreen.SelectCourse.route, navOptions)
}

fun NavGraphBuilder.selectCourseScreen(
    goToHome: () -> Unit
){
    composable(route = LeafScreen.SelectCourse.route) {
        SelectCourseRoute(goToHome)
    }
}

@Composable
fun SelectCourseRoute(
    goToHome:()->Unit,
    viewModel: SelectCourseViewModel = koinViewModel()
){
    val state = viewModel.uiState.collectAsState()
    SelectCourseScreen(
        state = state.value,
        onCourseSelected = {viewModel.handleIntent(SelectCourseIntent.OnCourseSelected(it))},
        onContinueClicked = {viewModel.handleIntent(SelectCourseIntent.OnContinueClicked(goToHome))},
        onBackClicked = {viewModel.handleIntent(SelectCourseIntent.OnBackClicked)}
    )
}