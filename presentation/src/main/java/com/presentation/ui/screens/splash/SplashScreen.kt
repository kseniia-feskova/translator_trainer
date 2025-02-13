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
import com.presentation.ui.bgColor
import com.presentation.ui.screens.auth.navigateToAuth
import com.presentation.ui.screens.home.navigateToHome
import com.presentation.ui.views.BackgroundDecorAnimated
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) { // Используем `true`, чтобы не пересоздавался при изменении переменных
        viewModel.isUserLoggedIn.collect {
            if (it) {
                delay(1500) // Имитация загрузки
                navController.navigateToHome(
                    navOptions = NavOptions.Builder().setPopUpTo(
                        navController.graph.startDestinationId,
                        inclusive = true
                    ).build()
                )
            } else {
                delay(1500) // Имитация загрузки
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
}

