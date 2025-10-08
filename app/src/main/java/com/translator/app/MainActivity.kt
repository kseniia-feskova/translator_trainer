package com.translator.app


import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.presentation.ui.AppTheme
import com.presentation.viewmodel.MainViewModel
import com.translator.app.di.databaseModule
import com.translator.app.di.networkModule
import com.translator.app.di.preferencesModule
import com.translator.app.di.repositoryModule
import com.translator.app.di.translateModule
import com.translator.app.di.viewModelModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import presentation.model.LessonType
import presentation.navigation.BottomNavigationBar
import presentation.navigation.LeafScreen
import presentation.navigation.TranslatorAppContainer
import presentation.ui.screens.auth.navigateToAuth
import useCaseModule

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route?.lowercase()

            LaunchedEffect(Unit) {
                viewModel.isUserAuthorized.collect {
                    if (!it) {
                        navController.navigateToAuth(
                            NavOptions.Builder()
                                .setPopUpTo(
                                    navController.graph.startDestinationId,
                                    inclusive = true
                                )
                                .setLaunchSingleTop(true)
                                .build()
                        )
                    }
                }
            }
            val shouldShowBottomBar = when {
                currentRoute?.contains(LeafScreen.NewSet.route) ?: true -> false
                currentRoute.contains(LeafScreen.Login.route) -> false
                currentRoute.contains(LeafScreen.VerifyEmail.route) -> false
                currentRoute.contains(LeafScreen.SelectCourse.route) -> false
                currentRoute.contains(LeafScreen.Splash.route) -> false
                currentRoute.contains(LeafScreen.Splash.route) -> false
                currentRoute.contains(LeafScreen.BubbleLesson("").route) -> false
                currentRoute.contains(LeafScreen.SuccessLesson(LessonType.BUBBLE, 0).route) -> false

                else -> true
            }
            AppTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    content = { padding ->
                        Box(modifier = Modifier.padding(padding)) {
                            TranslatorAppContainer(navController)
                        }
                    },
                    bottomBar = {
                        if (shouldShowBottomBar) {
                            BottomNavigationBar(navController = navController)
                        }
                    }

                )
            }
        }
    }
}


fun MainActivity.startKoin() {
    org.koin.core.context.startKoin {
        androidLogger(Level.ERROR)
        androidContext(applicationContext)
        modules(
            listOf(
                networkModule,
                translateModule,
                repositoryModule,
                databaseModule,
                useCaseModule,
                viewModelModule,
                preferencesModule
            )
        )
    }
}