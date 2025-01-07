package com.example.translatortrainer


import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.data.di.databaseModule
import com.data.di.networkModule
import com.data.di.repositoryModule
import com.data.di.translateModule
import com.example.translatortrainer.di.preferencesModule
import com.example.translatortrainer.di.useCaseModule
import com.example.translatortrainer.di.viewModelModule
import com.presentation.navigation.BottomNavigationBar
import com.presentation.navigation.LeafScreen
import com.presentation.navigation.TranslatorApp
import com.presentation.ui.AppTheme
import com.presentation.ui.screens.lesson.LessonType
import com.presentation.ui.screens.start.navigateToLogin
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class MainActivity : AppCompatActivity() {

    private val viewModel: com.presentation.viewmodel.MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin()
        viewModel.checkAndAddAllWordsSet()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route?.lowercase()
            Log.e(
                "MainActivity",
                "Current route = $currentRoute, lesson = ${
                    LeafScreen.Lesson(
                        0,
                        LessonType.TRANSLATE
                    ).route
                }"
            )
            val isUserAuthorized by viewModel.isUserAuthorized.collectAsState()

            LaunchedEffect(isUserAuthorized) {
                if (!isUserAuthorized) {
                    navController.navigateToLogin(
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
            val shouldShowBottomBar = when {
                currentRoute?.contains(LeafScreen.Lesson(0, LessonType.TRANSLATE).route) ?: true -> false
                currentRoute?.contains(LeafScreen.NewSet.route) ?: true -> false
                currentRoute?.contains(LeafScreen.Login.route) ?: true -> false
                else -> true
            }
            AppTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    content = { padding ->
                        Log.e("Preview", "Padding  = $padding")
                        Box(modifier = Modifier.padding(padding)) {
                            TranslatorApp(navController, isUserAuthorized)
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
    startKoin {
        androidLogger(Level.DEBUG)
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