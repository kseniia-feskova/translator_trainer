package presentation.viewmodel

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.prefs.IDataStoreManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import network.INetworkConnectivityObserver
import network.NetworkStatus
import presentation.model.FirebaseUser
import presentation.model.UserResult
import domain.usecases.auth.ICheckUserUseCase
import domain.usecases.auth.ILoginUseCase
import domain.usecases.auth.ISetGuestUseCase
import domain.usecases.course.ICoursesOnPrefsUseCases
import mapper.toUI

class MainViewModel(
    private val login: ILoginUseCase,
    private val guestUseCase: ISetGuestUseCase,
    private val coursesPrefs: ICoursesOnPrefsUseCases,
    private val networkObserver: INetworkConnectivityObserver,
    private val dataStore: IDataStoreManager,
    private val checkGoogleUser: ICheckUserUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val _googleSignEffect = Channel<GoogleSignUiEffect>()
    val googleSignEffect = _googleSignEffect.receiveAsFlow()

    init {
        checkNetworkConnection()
        checkAutorization()
    }

    fun onAppStart(intent: Intent?) {
        intent?.data?.let { uri ->
            handleDeepLink(uri)
        }
    }

    fun onNewIntent(intent: Intent) {
        intent.data?.let { uri ->
            handleDeepLink(uri)
        }
    }

    fun onUiEvent(event: MainUiEvent) {
        when (event) {
            is MainUiEvent.SignInWithGoogle -> {
                viewModelScope.launch {
                    _googleSignEffect.send(GoogleSignUiEffect.StartGoogleSignIn)
                }
            }

            is MainUiEvent.UserAuthorized -> {
                _uiState.update {
                    it.copy(isUserAuthorized = true)
                }
            }
        }
    }

    fun onGoogleAuthResult(user: FirebaseUser?) {
        if (user == null || user.uuid == null) return

        viewModelScope.launch {
            val result = checkGoogleUser.invoke(user.uuid).toUI()

            when (result) {
                is UserResult.Existing -> {
                    _uiState.update { it.copy(isNewUser = false, isUserAuthorized = true) }
                }

                is UserResult.New -> {
                    _uiState.update { it.copy(isNewUser = true, isUserAuthorized = true) }
                }

                is UserResult.Error -> {

                }
            }
        }
    }

    private fun handleDeepLink(uri: Uri) {
        Log.e("MainViewModel", "handleDeepLink $uri")
    }

    private fun checkAutorization() {
        viewModelScope.launch {
            login.listenUserId()
                .map { userId -> userId != null } // true, если userId не null
                .distinctUntilChanged()
                .collect { isAuthorized ->
                    viewModelScope.launch {
                        Log.e(
                            "MainViewModel",
                            "IsAuthorized = $isAuthorized, guest = ${guestUseCase.isGuestMode()}"
                        )
                        val check =
                            isAuthorized && (coursesPrefs.getCourse() != null) || guestUseCase.isGuestMode()
                        _uiState.update { it.copy(isUserAuthorized = check) }
                    }
                }
        }
    }

    private fun checkNetworkConnection() {
        viewModelScope.launch {
            networkObserver.observe().collect { status ->
                when (status) {
                    NetworkStatus.Available -> {
                        dataStore.setOfflineMode(false)
                        _uiState.update { it.copy(isInternetAvailable = true) }
                    }

                    NetworkStatus.Lost -> {
                        dataStore.setOfflineMode(true)
                        _uiState.update { it.copy(isInternetAvailable = false) }
                    }

                    NetworkStatus.Unavailable -> Log.e("MainVM", "Недоступно")
                    NetworkStatus.Losing -> Log.e("MainVM", "Сигнал теряется")
                }
            }
        }
    }
}

sealed class MainUiEvent() {
    object SignInWithGoogle : MainUiEvent()
    object UserAuthorized : MainUiEvent()
}

sealed interface GoogleSignUiEffect {
    object StartGoogleSignIn : GoogleSignUiEffect

}