package com.example.translatortrainer


import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.data.di.databaseModule
import com.data.di.networkModule
import com.data.di.repositoryModule
import com.example.translatortrainer.di.useCaseModule
import com.example.translatortrainer.di.viewModelModule
import com.presentation.navigation.BottomNavigationBar
import com.presentation.ui.AppTheme
import com.presentation.navigation.TranslatorApp
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
            AppTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    content = { padding ->
                        Log.e("Preview", "Padding  = $padding")
                        Box() {
                            TranslatorApp(navController)
                        }
                    },
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
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
                repositoryModule,
                databaseModule,
                useCaseModule,
                viewModelModule,
            )
        )
    }
}