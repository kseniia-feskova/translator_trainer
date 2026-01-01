package presentation.ui.screens.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.koin.androidx.compose.koinViewModel
import presentation.navigation.LeafScreen
import presentation.navigation.RootScreen
import presentation.viewmodel.MainUiEvent

fun NavController.navigateToAccount(
    navOptions: NavOptions? = null,
) {
    this.navigate(RootScreen.Profile.route, navOptions)
}

fun NavGraphBuilder.accountScreen(
    toLogin: () -> Unit,
    toHome: () -> Unit,
    onGoogleClick: (MainUiEvent) -> Unit
) {
    composable(route = LeafScreen.Account.route) {
        AccountRoute(
            toLogin = toLogin,
            toHome = toHome,
            onGoogleClick = onGoogleClick,
        )
    }
}

@Composable
fun AccountRoute(
    viewModel: AccountViewModel = koinViewModel(),
    toLogin: () -> Unit,
    toHome: () -> Unit,
    onGoogleClick: (MainUiEvent) -> Unit,
) {
    val authState by viewModel.authState.collectAsState()
    val state by viewModel.uiState.collectAsState()

    val onEditClicked = remember { { viewModel.handleIntent(AccountIntent.EditClicked) } }
    val changeTheme = remember { { viewModel.handleIntent(AccountIntent.ChangeTheme) } }
    val addLanguage = remember { { viewModel.handleIntent(AccountIntent.AddLanguage) } }
    val dismissDialog = remember { { viewModel.handleIntent(AccountIntent.DismissDialog) } }
    val logout = remember { { viewModel.handleIntent(AccountIntent.Logout(toLogin)) } }
    val deleteAccount = remember { { viewModel.handleIntent(AccountIntent.DeleteAccount) } }
    val createAccount = remember { { viewModel.handleIntent(AccountIntent.createAccount) } }
    val onEmailChanged =
        remember { { email: String -> viewModel.handleIntent(AccountIntent.OnEmailChanged(email)) } }
    val onPasswordChanged = remember {
        { password: String ->
            viewModel.handleIntent(
                AccountIntent.OnPasswordChanged(password)
            )
        }
    }
    val onAuthClicked = remember { { viewModel.handleIntent(AccountIntent.Auth({ toHome() })) } }
    val onAuthClose = remember { { viewModel.handleIntent(AccountIntent.onAuthClose) } }
    AccountScreen(
        name = state.name.toString(),
        image = state.image,
        guestData = state.guestData,
        btnsState = state.btnsState,
        authState = authState != null,
        authUIState = authState,
        onEditClicked = onEditClicked,
        changeTheme = changeTheme,
        addLanguage = addLanguage,
        dismissDialog = dismissDialog,
        logout = logout,
        deleteAccount = deleteAccount,
        createAccount = createAccount,
        onEmailChanged = onEmailChanged,
        onPasswordChanged = onPasswordChanged,
        onAuthClicked = onAuthClicked,
        onAuthClose = onAuthClose,
        onGoogleSignClick = {
            onGoogleClick.invoke(MainUiEvent.SignInWithGoogle)
        }
    )
}