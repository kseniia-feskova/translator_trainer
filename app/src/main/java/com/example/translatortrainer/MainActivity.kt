package com.example.translatortrainer


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.data.di.coreModule
import com.data.di.databaseModule
import com.data.di.networkModule
import com.data.di.repositoryModule
import com.data.di.translatorModule
import com.example.translatortrainer.di.viewModelModule
import com.example.translatortrainer.ui.core.TranslatorApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(applicationContext)
            modules(
                listOf(
                    coreModule,
                    repositoryModule,
                    viewModelModule,
                    databaseModule,
                    translatorModule,
                    networkModule
                )
            )
        }
        enableEdgeToEdge()
        setContent {
            TranslatorApp()
        }
    }
}