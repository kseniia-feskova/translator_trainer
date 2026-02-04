package presentation.ui.app

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.presentation.ui.screens.texts.photoToText
import presentation.model.LessonType
import presentation.navigation.AnimatedBottomBar
import presentation.navigation.LeafScreen
import presentation.navigation.RootScreen
import presentation.navigation.isBottomNavigationNeeded
import presentation.ui.screens.account.accountScreen
import presentation.ui.screens.account.navigateToAccount
import presentation.ui.screens.all.allWordsScreen
import presentation.ui.screens.all.navigateToAllWords
import presentation.ui.screens.auth.authScreen
import presentation.ui.screens.auth.navigateToAuth
import presentation.ui.screens.auth.verify.navigateToVerify
import presentation.ui.screens.auth.verify.verifyEmailScreen
import presentation.ui.screens.home.homeScreen
import presentation.ui.screens.home.navigateToHome
import presentation.ui.screens.lesson.bubble.bubbleLessonScreen
import presentation.ui.screens.lesson.bubble.navigateToBubbleLesson
import presentation.ui.screens.lesson.match.matchLessonScreen
import presentation.ui.screens.lesson.match.navigateToMatchLesson
import presentation.ui.screens.lesson.success.navigateToSuccessLesson
import presentation.ui.screens.lesson.success.successLessonScreen
import presentation.ui.screens.newset.navigateToNewSet
import presentation.ui.screens.newset.newSetScreen
import presentation.ui.screens.select_course.selectCourseScreen
import presentation.ui.screens.set.navigateToSet
import presentation.ui.screens.set.setScreen
import presentation.ui.screens.sets.setsScreen
import presentation.ui.screens.splash.splashScreen
import presentation.viewmodel.MainUiEvent
import presentation.viewmodel.MainUiState

@Composable
fun TranslatorAppHost(uiState: MainUiState, onUiEvent: (MainUiEvent) -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val bottomBarVisible =
        navBackStackEntry?.destination?.route?.isBottomNavigationNeeded() ?: false

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = {
            AnimatedBottomBar(
                bottomBarVisible = bottomBarVisible,
                navController = navController
            )
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
            navController = navController,
            startDestination = if (uiState.isUserAuthorized && uiState.isNewUser != null) {
                if (uiState.isNewUser) {
                    LeafScreen.SelectCourse.route
                } else {
                    LeafScreen.Home.route
                }
            } else {
                LeafScreen.Splash.route
            }
        ) {
            splashScreen(navController)
            loginNav(navController, onUiEvent)
            homeNav(navController)
            setsNav(navController)
            photoNav()
            accountNav(navController, onUiEvent)
        }
    }
}

private fun NavGraphBuilder.loginNav(
    navController: NavHostController,
    onUiEvent: (MainUiEvent) -> Unit
) {
    authScreen(
        goToCourses = {
            navController.navigate(LeafScreen.SelectCourse.route) {
                popUpTo(LeafScreen.Login.route) { inclusive = true }
            }
        },
        goToHome = {
            navController.navigate(RootScreen.Home.route) {
                popUpTo(LeafScreen.Login.route) { inclusive = true }
            }
        },
        goToVerification = navController::navigateToVerify,
        onGoogleClick = onUiEvent
    )
    selectCourseScreen(
        navigateUp = {
            navController.navigateToAuth(
                NavOptions.Builder()
                    .setPopUpTo(
                        navController.graph.startDestinationId,
                        inclusive = true
                    )
                    .setLaunchSingleTop(true)
                    .build()
            )
        },
        goToHome = {
            navController.navigate(RootScreen.Home.route) {
                popUpTo(LeafScreen.Login.route) { inclusive = true }
            }
        }
    )

    verifyEmailScreen(
        navigateUp = navController::navigateUp,
        navigateToCourses = {
            navController.navigate(LeafScreen.SelectCourse.route) {
                popUpTo(LeafScreen.Login.route) { inclusive = true }
            }
        }
    )
}

private fun NavGraphBuilder.homeNav(navController: NavHostController) {
    navigation(
        startDestination = LeafScreen.Home.route,
        route = RootScreen.Home.route
    ) {
        homeScreen(navController::navigateToAccount)
    }
}

private fun NavGraphBuilder.photoNav() {
    navigation(
        startDestination = LeafScreen.PhotoToText.route,
        route = RootScreen.Photo.route
    ) {
        photoToText()
    }
}

private fun NavGraphBuilder.setsNav(navController: NavHostController) {
    navigation(
        startDestination = LeafScreen.Sets.route, // Это для экрана
        route = RootScreen.Sets.route            // Это для графа
    ) {

        setsScreen(
            navigateToSelectedSet = { id, name ->
                navController.navigateToSet(id, name)
            },
            navigateToAllWordsSet = {
                navController.navigateToAllWords(it)
            },
            navigateToHome = navController::navigateToHome,
            createNewSet = { navController.navigateToNewSet() },
            navigateToLesson = { id, type ->
                when (type) {
                    LessonType.BUBBLE -> navController.navigateToBubbleLesson(id)
                    LessonType.MATCH -> navController.navigateToMatchLesson(id)
                }
            }
        )

        setScreen(
            navigateToLesson = { id, type ->
                when (type) {
                    LessonType.BUBBLE -> navController.navigateToBubbleLesson(id)
                    LessonType.MATCH -> navController.navigateToMatchLesson(id)
                }
            },
            navigateToEdit = {},
            navigateUp = { navController.navigateUp() },
        )


        successLessonScreen {
            navController.navigate(LeafScreen.Sets.route) {
                popUpTo(LeafScreen.Sets.route) { inclusive = true }
                launchSingleTop = true
            }
        }

        bubbleLessonScreen(
            navigateToSuccess = { type, words ->
                navController.navigateToSuccessLesson(words, type)
            },
            navigateUp = { navController.navigateUp() })

        matchLessonScreen(
            navigateToSuccess = { type, words ->
                navController.navigateToSuccessLesson(words, type)
            },
            navigateUp = { navController.navigateUp() })

        allWordsScreen {
            navController.navigateUp()
        }

        newSetScreen(
            navController::navigateUp,
            navController::navigateToAccount
        )
    }
}

private fun NavGraphBuilder.accountNav(
    navController: NavHostController, onUiEvent: (MainUiEvent) -> Unit,
) {
    navigation(
        startDestination = LeafScreen.Account.route, // Это для экрана
        route = RootScreen.Profile.route            // Это для графа
    ) {
        accountScreen(
            navController::navigateToAuth,
            navController::navigateToHome,
            onUiEvent,
        )
    }
}