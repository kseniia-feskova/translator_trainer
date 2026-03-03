package presentation.ui.app

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.presentation.ui.bgColor
import com.presentation.ui.darkColor
import com.presentation.ui.screens.texts.photoToText
import com.presentation.ui.whiteColor
import kotlinx.coroutines.launch
import presentation.model.LessonType
import presentation.navigation.LeafScreen
import presentation.navigation.RootScreen
import presentation.ui.AppTypography
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
import presentation.ui.screens.lesson.dictation.dictationLessonScreen
import presentation.ui.screens.lesson.dictation.navigateToDictationLesson
import presentation.ui.screens.lesson.dictation.result.dictationResultScreen
import presentation.ui.screens.lesson.dictation.result.navigateToDictationResult
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
    if (uiState.isUserAuthorized) {
        AuthorizedApp(navController, onUiEvent)
    } else {
        UnauthorizedApp(navController, onUiEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthorizedApp(
    navController: NavHostController,
    onUiEvent: (MainUiEvent) -> Unit
) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(RootScreen.Home) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    Log.e("TranslatorApp", "currentRoute = $currentRoute")
    val hideTopBar = currentRoute?.contains("BubbleLesson") == true ||
            currentRoute?.contains("MatchLesson") == true ||
            currentRoute?.contains("DictationLesson") == true
    ModalNavigationDrawer(
        gesturesEnabled = !hideTopBar,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = whiteColor,
                drawerContentColor = darkColor
            ) {
                DrawerHeader()
                DrawerBody(
                    RootScreen.entries.toList()
                ) {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    scope.launch {
                        selectedTab = it
                        drawerState.close()
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.systemBars,
            topBar = {
                if (!hideTopBar) {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors().copy(
                            containerColor = bgColor
                        ),
                        title = { Text(selectedTab.title) },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues),
                navController = navController,
                startDestination = RootScreen.Home.route
            ) {
                splashScreen(navController)
                // loginNav(navController, onUiEvent)
                homeNav(navController)
                setsNav(navController)
                photoNav()
                accountNav(navController, onUiEvent)
            }
        }
    }
}

@Composable
private fun UnauthorizedApp(
    navController: NavHostController,
    onUiEvent: (MainUiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
            navController = navController,
            startDestination = LeafScreen.Splash.route
        ) {
            splashScreen(navController)
            loginNav(navController, onUiEvent)
            // homeNav(navController)
        }
    }
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Translator Trainer",
            style = AppTypography.displayMedium,
            modifier = Modifier.padding(vertical = 32.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(darkColor)
                .padding(horizontal = 6.dp)
        )
    }
}

@Composable
@Preview
fun DrawerHeaderPreview() {
    Column {
        DrawerHeader()
        DrawerBody(
            RootScreen.entries.toList()
        ) {}
    }
}

@Composable
fun DrawerBody(
    items: List<RootScreen>,
    modifier: Modifier = Modifier,
    onItemClick: (RootScreen) -> Unit
) {
    LazyColumn(modifier) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(item.iconRes),
                    contentDescription = item.route,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    modifier = Modifier.weight(1f),
                    style = AppTypography.displaySmall
                )
            }
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
            onUiEvent.invoke(MainUiEvent.UserAuthorized)
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
            onUiEvent.invoke(MainUiEvent.UserAuthorized)
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
            createNewSet = { navController.navigateToNewSet() }
        )

        setScreen(
            navigateToLesson = { id, type ->
                when (type) {
                    LessonType.BUBBLE -> navController.navigateToBubbleLesson(id)
                    LessonType.MATCH -> navController.navigateToMatchLesson(id)
                    LessonType.DICTATION -> navController.navigateToDictationLesson(id)
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

        dictationResultScreen {
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

        dictationLessonScreen(
            navigateToSuccess = { type ->
                navController.navigateToDictationResult(type)
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