package com.presentation.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.presentation.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel

fun NavController.navigateToAuth(
    navOptions: NavOptions? = null,
) {
    this.navigate(LeafScreen.Login.route, navOptions)
}

fun NavGraphBuilder.authScreen(
    onAuth: () -> Unit
) {
    composable(route = LeafScreen.Login.route) { AuthRoute(onAuth) }
}

@Composable
fun AuthRoute(
    //TODO: onLoginSuccess we need to check courseId in prefs and navigate to CourseSelection or to HomeScreen
    onAuth: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    AuthScreen(
        state = state.value,
        onEmailChanged = { viewModel.handleIntent(AuthIntent.OnEmailChanged(it)) },
        onPasswordChanged = { viewModel.handleIntent(AuthIntent.OnPasswordChanged(it)) },
        onAuthStateChanged = { viewModel.handleIntent(AuthIntent.ChangeScreen) },
        onAuthClicked = { viewModel.handleIntent(AuthIntent.Auth) },
    )

}