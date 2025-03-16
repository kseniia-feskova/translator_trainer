package com.presentation.ui.screens.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.usecases.IAccountUseCase
import com.presentation.usecases.IGetAccountUseCase
import com.presentation.usecases.auth.IDeleteUseCase
import com.presentation.usecases.auth.ILogoutUseCase
import com.presentation.usecases.auth.ISetGuestUseCase
import com.presentation.usecases.course.ICoursesOnPrefsUseCases
import com.presentation.usecases.sets.IGetAllSetsUseCase
import com.presentation.utils.ALL_WORDS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URL
import java.util.UUID

class AccountViewModel(
    private val getDetails: IGetAccountUseCase,
    private val logout: ILogoutUseCase,
    private val deleteAccount: IDeleteUseCase,
    private val guestUseCase: ISetGuestUseCase,
    private val accountPrefs: IAccountUseCase,
    private val coursePrefs: ICoursesOnPrefsUseCases,
    private val getSets: IGetAllSetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUIState(loading = true))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (guestUseCase.isGuestMode()) {
                val course = coursePrefs.getCourse()
                if (course != null) {
                    val sets = getSets.invoke(UUID.fromString(course.id)).getOrNull()
                    val allSet = sets?.find { it.title == ALL_WORDS }
                    val guestData = GuestData(
                        course = course,
                        allSetsCount = sets?.size ?: 0,
                        allWordsCount = allSet?.words?.size ?: 0
                    )
                    Log.e("AccountViewModel", "Guest = $guestData")
                    _uiState.update {
                        it.copy(guestData = guestData)
                    }
                }
            } else {
                val userId = accountPrefs.getUserId()
                if (userId != null) {
                    val response = getDetails.invoke(userId)
                    if (response.isSuccess) {
                        val user = response.getOrNull()
                        if (user != null) {
                            _uiState.update {
                                it.copy(
                                    loading = false,
                                    name = user.username,
                                    image = if (user.photo == null) null else URL(user.photo)
                                )
                            }
                        } else {
                            Log.e("AccountViewModel", "User is null")
                        }
                    } else {
                        Log.e(
                            "AccountViewModel",
                            "response is failed, ${response.exceptionOrNull()}"
                        )
                    }
                }
            }
        }
    }

    fun handleIntent(intent: AccountIntent) {
        when (intent) {
            AccountIntent.AddLanguage -> {
                //navigate to add language
            }

            AccountIntent.ChangeTheme -> {
                //navigate to change theme
            }

            //show warning dialog before deletion
            AccountIntent.DeleteAccount -> {
                val isDialogVisible = _uiState.value.showDeleteDialog
                if (isDialogVisible) {
                    viewModelScope.launch {
                        deleteAccount.invoke()
                        //  dataStoreManager.saveUserId(null)
                        _uiState.update {
                            it.copy(showDeleteDialog = false)
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(showDeleteDialog = true)
                    }
                }
            }

            AccountIntent.EditClicked -> {
                //navigate to edit profile
            }

            //show warning dialog before logout
            AccountIntent.Logout -> {
                val isDialogVisible = _uiState.value.showLogoutDialog
                if (isDialogVisible) {
                    viewModelScope.launch {
                        logout.invoke()
                        _uiState.update {
                            it.copy(showLogoutDialog = false)
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(showLogoutDialog = true)
                    }
                }
            }

            AccountIntent.DismissDialog -> {
                _uiState.update {
                    it.copy(showLogoutDialog = false, showDeleteDialog = false)
                }
            }
        }
    }
}