package com.presentation.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.translator.app.ui.bgColor
import com.translator.app.ui.screens.splash.SplashViewModel
import com.translator.app.ui.screens.auth.navigateToAuth
import com.translator.app.ui.screens.home.navigateToHome
import com.translator.app.ui.views.BackgroundDecorAnimated

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.isUserLoggedIn.collect {
            if (it) {
                navController.navigateToHome(
                    navOptions = NavOptions.Builder().setPopUpTo(
                        navController.graph.startDestinationId,
                        inclusive = true
                    ).build()
                )
            } else {
                navController.navigateToAuth(
                    navOptions = NavOptions.Builder().setPopUpTo(
                        navController.graph.startDestinationId,
                        inclusive = true
                    ).build()
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center,
    ) {
        BackgroundDecorAnimated()
    }

    LaunchedEffect(true) {
        viewModel.observeUserId()
    }
}

