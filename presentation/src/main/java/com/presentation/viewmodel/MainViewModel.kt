package com.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.usecases.auth.ILoginUseCase
import com.presentation.usecases.auth.ISetGuestUseCase
import com.presentation.usecases.course.ICoursesOnPrefsUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataStorage: ILoginUseCase,
    private val guestUseCase: ISetGuestUseCase,
    private val coursesPrefs: ICoursesOnPrefsUseCases
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
                        Log.e("MainViewModel", "IsAuthorized = $isAuthorized, guest = ${guestUseCase.isGuestMode()}")
                        val check = isAuthorized && (coursesPrefs.getCourse() != null) || guestUseCase.isGuestMode()
                        _isUserAuthorized.emit(check)
                    }
                }
        }
    }

}