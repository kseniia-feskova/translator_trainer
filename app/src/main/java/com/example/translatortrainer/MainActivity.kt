package com.example.translatortrainer


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.data.di.databaseModule
import com.data.di.networkModule
import com.data.di.repositoryModule
import com.domain.di.useCaseModule
import com.example.translatortrainer.di.appModule
import com.example.translatortrainer.di.viewModelModule
import com.example.translatortrainer.ui.core.TranslatorApp
import com.example.translatortrainer.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin()
        viewModel.checkAndAddAllWordsSet()

        enableEdgeToEdge()
        setContent {
            TranslatorApp()
        }
    }
}


fun MainActivity.startKoin() {
    startKoin {
        androidLogger(Level.DEBUG)
        androidContext(applicationContext)
        modules(
            listOf(
                appModule,
                networkModule,
                repositoryModule,
                databaseModule,
                useCaseModule,
                viewModelModule,
            )
        )
    }
}