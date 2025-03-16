package com.presentation.ui.screens.account

import com.presentation.model.CourseUI
import java.net.URL

data class AccountUIState(
    val name: String? = null,
    val image: URL? = null,
    val loading: Boolean,
    val showLogoutDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val guestData: GuestData? = null
)

data class GuestData(
    val course: CourseUI,
    val allWordsCount: Int,
    val allSetsCount: Int
)

sealed class AccountIntent {
    object EditClicked : AccountIntent()
    object AddLanguage : AccountIntent()
    object ChangeTheme : AccountIntent()
    object Logout : AccountIntent()
    object DeleteAccount : AccountIntent()
    object DismissDialog : AccountIntent()
}