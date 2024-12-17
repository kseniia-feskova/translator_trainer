package com.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.presentation.R
import com.presentation.ui.AppTheme
import com.presentation.ui.accentColorLight
import com.presentation.ui.onSurfaceLight
import com.presentation.ui.primaryColorLight
import com.presentation.ui.screens.lesson.LessonType
import com.presentation.ui.surfaceLight
import kotlinx.serialization.Serializable

enum class RootScreen(val route: String, val iconRes: Int) {
    Home("home_root", R.drawable.ic_translate),
    Sets("sets_root", R.drawable.ic_sets),
    Profile("profile_root", R.drawable.ic_account)
}

@Serializable
sealed class LeafScreen(val route: String) {
    object Home : LeafScreen("home")
    object Sets : LeafScreen("sets")

    @Serializable
    data class Set(val setId: Int) : LeafScreen("set")

    @Serializable
    data class Lesson(val setId: Int, val type: LessonType) : LeafScreen("lesson")

    @Serializable
    data class AllWords(val setId: Int) : LeafScreen("allWords")

    object NewSet : LeafScreen("newset")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        modifier = Modifier.height(60.dp)
            .clip(
                shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp),
            ),
        containerColor = primaryColorLight
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        RootScreen.values().forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.matchDestination(item.route) ?: false,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painterResource(item.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIconColor = onSurfaceLight,
                    selectedIndicatorColor = Color.Transparent,
                    unselectedIconColor = accentColorLight,
                ),
            )
        }
    }
}

@Composable
@Preview
fun BottomBarPreview() {
    AppTheme {
        Surface() {
            Column(modifier = Modifier.background(surfaceLight)) {
                Spacer(modifier = Modifier.size(30.dp))
                BottomNavigationBar(navController = NavController(LocalContext.current))
            }
        }
    }
}

fun NavDestination?.matchDestination(route: String): Boolean {
    var currentDestination = this
    while (currentDestination != null && currentDestination.route != route) {
        currentDestination = currentDestination.parent
    }
    return currentDestination?.route == route
}