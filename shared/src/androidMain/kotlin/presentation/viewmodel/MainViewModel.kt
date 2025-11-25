package presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.prefs.IDataStoreManager
import presentation.usecases.auth.ILoginUseCase
import presentation.usecases.auth.ISetGuestUseCase
import presentation.usecases.course.ICoursesOnPrefsUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import network.INetworkConnectivityObserver
import network.NetworkStatus

class MainViewModel(
    private val login: ILoginUseCase,
    private val guestUseCase: ISetGuestUseCase,
    private val coursesPrefs: ICoursesOnPrefsUseCases,
    private val networkObserver: INetworkConnectivityObserver,
    private val dataStore: IDataStoreManager
) : ViewModel() {

    private val _isUserAuthorized = MutableSharedFlow<Boolean>()
    val isUserAuthorized = _isUserAuthorized.asSharedFlow()

    private val _isNetworkConnected = MutableSharedFlow<Boolean>()
    val isNetworkConnected = _isNetworkConnected.asSharedFlow()


    init {
        observeUserId()
        observeNetwork()
    }

    private fun observeUserId() {
        viewModelScope.launch {
            login.listenUserId()
                .map { userId -> userId != null } // true, если userId не null
                .distinctUntilChanged() // Отслеживаем только изменения
                .collect { isAuthorized ->
                    viewModelScope.launch {
                        Log.e(
                            "MainViewModel",
                            "IsAuthorized = $isAuthorized, guest = ${guestUseCase.isGuestMode()}"
                        )
                        val check =
                            isAuthorized && (coursesPrefs.getCourse() != null) || guestUseCase.isGuestMode()
                        _isUserAuthorized.emit(check)
                    }
                }
        }
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkObserver.observe().collect { status ->
                when (status) {
                    NetworkStatus.Available -> {
                        Log.e("MainVM", "Интернет есть")
                        dataStore.setOfflineMode(false)
                        _isNetworkConnected.emit(true)
                    }

                    NetworkStatus.Lost -> {
                        Log.e("MainVM", "Интернет пропал")
                        dataStore.setOfflineMode(true)
                        _isNetworkConnected.emit(false)
                    }

                    NetworkStatus.Unavailable -> Log.e("MainVM", "Недоступно")
                    NetworkStatus.Losing -> Log.e("MainVM", "Сигнал теряется")
                }
            }
        }
    }
}