package com.presentation.ui.screens.auth

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
    goToCourses: () -> Unit,
    goToHome: () -> Unit
) {
    composable(route = LeafScreen.Login.route,
        enterTransition = { fadeIn(animationSpec = tween(1000)) },
        exitTransition = { fadeOut(animationSpec = tween(500)) }) {
        AuthRoute(
            goToCourses,
            goToHome
        )
    }
}

@Composable
fun AuthRoute(
    goToCourses: () -> Unit,
    goToHome: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    AuthScreen(
        state = state.value,
        onEmailChanged = { viewModel.handleIntent(AuthIntent.OnEmailChanged(it)) },
        onPasswordChanged = { viewModel.handleIntent(AuthIntent.OnPasswordChanged(it)) },
        onAuthStateChanged = { viewModel.handleIntent(AuthIntent.ChangeScreen) },
        onAuthClicked = { viewModel.handleIntent(AuthIntent.Auth(goToCourses, goToHome)) },
        onGuestSelected = { viewModel.handleIntent(AuthIntent.SaveAsGuest(goToCourses)) }
    )

}