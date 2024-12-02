package com.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.presentation.ui.surfaceLight

enum class BottomNavItem(val route: String, val iconRes: Int, val label: String) {
    Home(homeScreen, R.drawable.ic_translate, "Home"),
    Sets("sets", R.drawable.ic_sets, "Sets"),
    Profile("profile", R.drawable.ic_account, "Profile")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        modifier = Modifier
            .height(36.dp)
            .clip(
                shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp),
            ),
        backgroundColor = primaryColorLight
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        BottomNavItem.values().forEach { item ->
            BottomNavigationItem(
                modifier = Modifier.padding(top = 12.dp),
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
                icon = { Icon(painterResource(item.iconRes), contentDescription = null) },
                selectedContentColor = onSurfaceLight,
                unselectedContentColor = accentColorLight
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