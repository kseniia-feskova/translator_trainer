package presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.wear.compose.navigation.currentBackStackEntryAsState
import com.example.translatortrainer.shared.R
import com.presentation.ui.AppTheme
import com.presentation.ui.accentColorLight
import com.presentation.ui.onSurfaceLight
import com.presentation.ui.surfaceLight
import kotlinx.serialization.Serializable
import presentation.model.LessonType

enum class RootScreen(val route: String, val iconRes: Int, val title: String) {
    Home("home_root", R.drawable.ic_translate, "Translator"),
    Sets("sets_root", R.drawable.ic_sets, "Exercises"),
    Photo("photo_root", R.drawable.ic_camera, "Photo translating"),
    Profile("profile_root", R.drawable.ic_account, "Profile")
}

@Serializable
sealed class LeafScreen(val route: String) {
    data object Splash : LeafScreen("splash")
    data object Login : LeafScreen("login")
    data object VerifyEmail : LeafScreen("verify_email")
    data object SelectCourse : LeafScreen("select_course")
    data object Home : LeafScreen("home")
    data object Sets : LeafScreen("sets")
    data object Account : LeafScreen("account")
    data object PhotoToText : LeafScreen("photo_to_text")

    @Serializable
    data class Set(val setId: String, val setName: String) : LeafScreen("set")

    //  @Serializable
    // data class Lesson(val setId: String, val type: LessonType) : LeafScreen("lesson")

    @Serializable
    data class BubbleLesson(val setId: String) : LeafScreen("bubble")

    @Serializable
    data class MatchLesson(val setId: String) : LeafScreen("match")

    @Serializable
    data class DictationLesson(val setId: String) : LeafScreen("dictation")

    @Serializable
    data class SuccessLesson(
        val type: LessonType,
        val wordsCount: Int
    ) : LeafScreen("success")

    @Serializable
    data class DictationResult(val type: LessonType) : LeafScreen("dictationResult")

    @Serializable
    data class AllWords(val setId: String) : LeafScreen("allWords")

    object NewSet : LeafScreen("newset")
}

@Composable
fun BottomNavigationBar(bgColor: Color = Color.White, navController: NavController) {
    NavigationBar(
        containerColor = bgColor
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        RootScreen.entries.forEach { item ->
            NavigationBarItem(
                modifier = Modifier.height(48.dp),
                selected = currentDestination?.matchDestination(item.route) ?: false,
                onClick = {
                    val currentRoute = currentDestination?.parent?.route
                    if (currentRoute == item.route) {
                        navController.navigate(item.route) {
                            popUpTo(item.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
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
        Surface {
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