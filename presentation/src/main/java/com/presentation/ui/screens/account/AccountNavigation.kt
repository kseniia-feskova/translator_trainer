package com.presentation.ui.screens.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.presentation.navigation.LeafScreen
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.accountScreen(
) {
    composable(route = LeafScreen.Account.route) { AccountRoute() }
}

@Composable
fun AccountRoute(
    viewModel: AccountViewModel = koinViewModel(),
) {
    val state = viewModel.uiState.collectAsState()
    AccountScreen(
        state = state.value,
        onEditClicked = { viewModel.handleIntent(AccountIntent.EditClicked) },
        changeTheme = { viewModel.handleIntent(AccountIntent.ChangeTheme) },
        addLanguage = { viewModel.handleIntent(AccountIntent.AddLanguage) },
        dismissDialog = { viewModel.handleIntent(AccountIntent.DismissDialog) },
        logout = { viewModel.handleIntent(AccountIntent.Logout) },
        deleteAccount = { viewModel.handleIntent(AccountIntent.DeleteAccount) }
    )
}