package com.presentation.ui.screens.auth.verify

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.presentation.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel

fun NavController.navigateToVerify(
    navOptions: NavOptions? = null,
) {
    this.navigate(LeafScreen.VerifyEmail.route, navOptions)
}

fun NavGraphBuilder.verifyEmailScreen(
    navigateUp: () -> Unit,
    navigateToCourses: () -> Unit
) {
    composable(route = LeafScreen.VerifyEmail.route) {
        VerifyRoute(
            navigateUp = navigateUp,
            navigateToCourses = navigateToCourses
        )
    }
}

@Composable
fun VerifyRoute(
    viewModel: VerifyEmailViewModel = koinViewModel(),
    navigateUp: () -> Unit,
    navigateToCourses: () -> Unit,
) {
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val enteredCode by viewModel.enteredCode.collectAsState()
    val timerVisible by viewModel.timerVisible.collectAsState()

    val onCodeEntering =
        remember { { code: String -> viewModel.handleIntent(VerifyIntent.OnCodeEntering(code)) } }
    val onBackClicked =
        remember { { viewModel.handleIntent(VerifyIntent.OnBackClicked(navigateUp)) } }
    val onVerifyClicked =
        remember { { viewModel.handleIntent(VerifyIntent.OnVerifyClicked(navigateToCourses)) } }
    val resendCode = remember { { viewModel.handleIntent(VerifyIntent.ResendCodeClicked) } }
    val timeout = remember { { viewModel.handleIntent(VerifyIntent.Timeout) } }

    VerifyEmailScreen(
        loading = loading,
        error = error,
        enteredCode = enteredCode,
        timerVisible = timerVisible,
        onCodeEntering = onCodeEntering,
        back = onBackClicked,
        verifyCode = onVerifyClicked,
        resendCode = resendCode,
        onTimeout = timeout
    )
}