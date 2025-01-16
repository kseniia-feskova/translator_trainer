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
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    LoginScreen(
        state = state.value,
        onUsernameChanged = { viewModel.handleIntent(LoginIntent.UsernameChanged(it)) },
        onEmailChanged = { viewModel.handleIntent(LoginIntent.EmailChanged(it)) },
        onPasswordChanged = { viewModel.handleIntent(LoginIntent.PasswordChanged(it)) },
        onSecondPasswordChanged = { viewModel.handleIntent(LoginIntent.RepeatPasswordChanged(it)) },
        onSwitchAuth = { viewModel.handleIntent(LoginIntent.SwitchAuth) },
        onEnterClicked = { viewModel.handleIntent(LoginIntent.EnterClicked(onLoginSuccess)) },
        onCourseSelected = {viewModel.handleIntent(LoginIntent.SelectCourse(it))}
    )

}