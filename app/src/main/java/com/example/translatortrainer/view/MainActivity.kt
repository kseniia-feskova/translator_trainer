package com.example.translatortrainer.view


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.translatortrainer.R
import com.example.translatortrainer.databinding.ActivityMainBinding
import com.example.translatortrainer.di.databaseModule
import com.example.translatortrainer.di.repositoryModule
import com.example.translatortrainer.di.translatorModule
import com.example.translatortrainer.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(applicationContext)
            modules(listOf(repositoryModule, viewModelModule, databaseModule, translatorModule))
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWithNavController(binding.bottomNavigation, getNavigator())
    }

    fun getNavigator(): NavController {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }

    fun hideBottomMenu() {
        binding.bottomNavigation.visibility = View.GONE
    }

    fun showBottomMenu() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }
}