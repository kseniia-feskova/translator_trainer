package com.presentation.ui.screens.account

import androidx.compose.runtime.Stable
import com.presentation.model.CourseUI
import java.net.URL

data class AccountUIState(
    val name: String? = null,
    val image: URL? = null,
    val loading: Boolean,
    val guestData: GuestData? = null,
    val btnsState: AccountBtnsState = AccountBtnsState()
)

data class AccountBtnsState(
    val showLogoutDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showAuthScreen: Boolean = false
)

@Stable
data class GuestData(
    val course: CourseUI,
    val allWordsCount: Int,
    val allSetsCount: Int
)

sealed class AccountIntent {
    object EditClicked : AccountIntent()
    object AddLanguage : AccountIntent()
    object ChangeTheme : AccountIntent()
    data class Logout(val goToAuth: () -> Unit) : AccountIntent()
    object DeleteAccount : AccountIntent()
    object DismissDialog : AccountIntent()

    object onAuthClose : AccountIntent()
    object createAccount : AccountIntent()

    data class OnEmailChanged(val login: String) : AccountIntent()
    data class OnPasswordChanged(val password: String) : AccountIntent()

    data class Auth(val goToHome: () -> Unit) : AccountIntent()
}