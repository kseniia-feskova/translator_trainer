package presentation.ui.screens.account

import android.net.Uri
import androidx.compose.runtime.Stable
import presentation.model.CourseUI
import presentation.model.FirebaseUser
import presentation.viewmodel.MainUiEvent

data class AccountUIState(
    val name: String? = null,
    val image: Uri? = null,
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
    val course: CourseUI? = null,
    val allWordsCount: Int,
    val allSetsCount: Int
)

sealed class AccountIntent {
    data object EditClicked : AccountIntent()
    data object AddLanguage : AccountIntent()
    data object ChangeTheme : AccountIntent()
    data class Logout(val goToAuth: (MainUiEvent) -> Unit) : AccountIntent()
    data object DeleteAccount : AccountIntent()
    data object DismissDialog : AccountIntent()

    data object onAuthClose : AccountIntent()
    data object createAccount : AccountIntent()

    data class OnEmailChanged(val login: String) : AccountIntent()
    data class OnPasswordChanged(val password: String) : AccountIntent()

    data class Auth(val goToHome: () -> Unit) : AccountIntent()

    data class GoogleSign(val firebaseUser: FirebaseUser?, val goToHome: () -> Unit) :
        AccountIntent()
}