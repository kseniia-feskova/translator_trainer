package com.presentation.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val dataStorage: IDataStoreManager
) : ViewModel() {

    private val _isUserLoggedIn = MutableSharedFlow<Boolean>()
    val isUserLoggedIn = _isUserLoggedIn.asSharedFlow()

    init {
        observeUserId()
    }

    private fun observeUserId() {
        viewModelScope.launch {
            dataStorage.listenUserId().collect {
                _isUserLoggedIn.emit(it != null && dataStorage.getCourse() != null)
            }
        }
    }

}