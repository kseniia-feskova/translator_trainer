package com.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataStorage: IDataStoreManager
) : ViewModel() {

    private val _isUserAuthorized = MutableSharedFlow<Boolean>()
    val isUserAuthorized = _isUserAuthorized.asSharedFlow()

    init {
        observeUserId()
    }

    private fun observeUserId() {
        viewModelScope.launch {
            dataStorage.listenUserId()
                .map { userId -> userId != null } // true, если userId не null
                .distinctUntilChanged() // Отслеживаем только изменения
                .collect { isAuthorized ->
                    viewModelScope.launch {
                        Log.e("MainViewModel", "IsAuthorized = $isAuthorized")
                        val check = isAuthorized && (dataStorage.getCourse() != null)
                        _isUserAuthorized.emit(check)
                    }
                }
        }
    }

}