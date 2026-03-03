package com.translator.app


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.presentation.ui.AppTheme
import com.translator.app.di.databaseModule
import com.translator.app.di.networkModule
import com.translator.app.di.preferencesModule
import com.translator.app.di.repositoryModule
import com.translator.app.di.translateModule
import com.translator.app.di.viewModelModule
import credential.GoogleSignInManager
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import presentation.ui.app.TranslatorAppHost
import presentation.viewmodel.GoogleSignUiEffect
import presentation.viewmodel.MainViewModel
import useCaseModule

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()

    private lateinit var authCoordinator: GoogleSignInManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin()
        viewModel.onAppStart(intent)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        authCoordinator = GoogleSignInManager(this) {
            viewModel.onGoogleAuthResult(it)
        }

        lifecycleScope.launch {
            viewModel.googleSignEffect.collect { effect ->
                when (effect) {
                    GoogleSignUiEffect.StartGoogleSignIn -> {
                        authCoordinator.launchSignIn()
                    }
                }
            }
        }
        setContent {
            AppTheme {
                val uiState by viewModel.uiState.collectAsState()
                Box(modifier = Modifier.fillMaxSize()) {
                    Log.e("MainActivity", "uiState = ${uiState.isUserAuthorized}")
                    TranslatorAppHost(
                        uiState = uiState,
                        onUiEvent = viewModel::onUiEvent
                    )

                    if (!uiState.isInternetAvailable) {
                        Snackbar(
                            modifier = Modifier.padding(16.dp),
                            action = {},
                        ) {
                            Text("No internet connection")
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        viewModel.onNewIntent(intent)
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