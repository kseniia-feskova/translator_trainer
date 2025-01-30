package com.presentation.ui.screens.start

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.presentation.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel

fun NavController.navigateToLogin(
    navOptions: NavOptions? = null,
) {
    this.navigate(LeafScreen.Login.route, navOptions)
}

fun NavGraphBuilder.loginScreen(
    onLoginSuccess: () -> Unit
) {
    composable(route = LeafScreen.Login.route) { LoginRoute(onLoginSuccess) }
}

@Composable
fun LoginRoute(
    //TODO: onLoginSuccess we need to check courseId in prefs and navigate to CourseSelection or to HomeScreen
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    LoginScreen(
        state = state.value,
        onEmailChanged = { viewModel.handleIntent(LoginIntent.OnLoginChanged(it)) },
        onPasswordChanged = { viewModel.handleIntent(LoginIntent.OnPasswordChanged(it)) },
        onLoginClicked = { viewModel.handleIntent(LoginIntent.Login) },
    )

}