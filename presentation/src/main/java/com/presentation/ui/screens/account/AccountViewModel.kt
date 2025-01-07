package com.presentation.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import com.presentation.usecases.auth.IDeleteUseCase
import com.presentation.usecases.auth.ILogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val logout: ILogoutUseCase,
    private val deleteAccount: IDeleteUseCase,
    private val dataStoreManager: IDataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUIState(loading = true))
    val uiState = _uiState.asStateFlow()

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
                        dataStoreManager.saveUserId(null)
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
                        dataStoreManager.saveUserId(null)
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