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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.presentation.R
import com.presentation.ui.AppTheme
import com.presentation.ui.accentColorLight
import com.presentation.ui.onSurfaceLight
import com.presentation.ui.primaryColorLight
import com.presentation.ui.screens.home.homeScreen
import com.presentation.ui.screens.sets.setsScreen
import com.presentation.ui.surfaceLight

enum class BottomNavItem(val route: String, val iconRes: Int) {
    Home(homeScreen, R.drawable.ic_translate),
    Sets(setsScreen, R.drawable.ic_sets),
    Profile("profile", R.drawable.ic_account)
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
        val currentRoute = navBackStackEntry?.destination?.route

        BottomNavItem.values().forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
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