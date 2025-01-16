package com.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataStorage: IDataStoreManager
) : ViewModel() {

    private val _isUserAuthorized = MutableStateFlow(false)
    val isUserAuthorized: StateFlow<Boolean> = _isUserAuthorized.asStateFlow()

    init {
        observeUserId()
    }

    private fun observeUserId() {
        viewModelScope.launch {
            dataStorage.listenUserId()
                .map { userId -> userId != null } // true, если userId не null
                .distinctUntilChanged() // Отслеживаем только изменения
                .collect { isAuthorized ->
                    Log.e("MainViewModel", "IsAuthorized = $isAuthorized")
                    _isUserAuthorized.value = isAuthorized
                }
        }
    }

    //создание массивов, если их нет
    fun checkAndAddAllWordsSet() {

    }
}