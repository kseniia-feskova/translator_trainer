package com.translator.app.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.usecases.auth.ISetGuestUseCase
import domain.usecases.course.ICoursesOnPrefsUseCases
import domain.usecases.user.IListenUserIdUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val coursesPrefs: ICoursesOnPrefsUseCases,
    private val authPrefs: ISetGuestUseCase,
    private val listenUserId: IListenUserIdUseCase
) : ViewModel() {

    private val _isUserLoggedIn = MutableSharedFlow<Boolean>()
    val isUserLoggedIn = _isUserLoggedIn.asSharedFlow()

    fun observeUserId() {
        viewModelScope.launch {
            delay(1500) // Задержка перед проверкой авторизации
            listenUserId.invoke().collect {
                _isUserLoggedIn.emit((it != null && coursesPrefs.getCourse() != null) || authPrefs.isGuestMode())
            }
        }
    }

}