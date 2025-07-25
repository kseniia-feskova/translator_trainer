package presentation.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import presentation.usecases.auth.ILoginUseCase
import presentation.usecases.auth.ISetGuestUseCase
import presentation.usecases.course.ICoursesOnPrefsUseCases
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val login: ILoginUseCase,
    private val coursesPrefs: ICoursesOnPrefsUseCases,
    private val authPrefs: ISetGuestUseCase
) : ViewModel() {

    private val _isUserLoggedIn = MutableSharedFlow<Boolean>()
    val isUserLoggedIn = _isUserLoggedIn.asSharedFlow()

//    init {
//        observeUserId()
//    }

    fun observeUserId() {
        viewModelScope.launch {
            delay(1500) // Задержка перед проверкой авторизации
            login.listenUserId().collect {
                _isUserLoggedIn.emit((it != null && coursesPrefs.getCourse() != null) || authPrefs.isGuestMode())
            }
        }
    }

}