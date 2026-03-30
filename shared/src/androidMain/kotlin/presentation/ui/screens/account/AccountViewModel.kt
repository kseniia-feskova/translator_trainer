package presentation.ui.screens.account

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import presentation.ui.screens.auth.AuthError
import presentation.ui.screens.auth.AuthScreenState
import presentation.ui.screens.auth.AuthUIState
import domain.usecases.IAccountUseCase
import domain.usecases.IGetAccountUseCase
import domain.usecases.auth.ICreateFromGuestUseCase
import domain.usecases.auth.IDeleteUseCase
import domain.usecases.auth.ILogoutUseCase
import domain.usecases.auth.ISetGuestUseCase
import domain.usecases.course.ICoursesOnPrefsUseCases
import domain.usecases.sets.IGetAllSetsUseCase
import domain.ALL_WORDS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.model.FirebaseUser
import domain.usecases.auth.IRegisterWithFirebaseUseCase
import mapper.toData
import mapper.toUI

class AccountViewModel(
    coursePrefs: ICoursesOnPrefsUseCases,
    getSets: IGetAllSetsUseCase,
    private val getDetails: IGetAccountUseCase,
    private val logout: ILogoutUseCase,
    private val deleteAccount: IDeleteUseCase,
    private val guestUseCase: ISetGuestUseCase,
    private val accountPrefs: IAccountUseCase,
    private val createUser: ICreateFromGuestUseCase,
    private val registerByFirebase: IRegisterWithFirebaseUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthUIState?>(null)
    val authState = _authState.asStateFlow()

    private val isGuestMode = guestUseCase.isGuestModeFlow()
    private val course = coursePrefs.getCourseFlow().map { it?.toUI() }
    private val guestDataFlow = getSets.invokeFlow().map { sets ->
        val allWordsSet = sets.find { it.name == ALL_WORDS }
        GuestData(
            allSetsCount = sets.size,
            allWordsCount = allWordsSet?.words?.size ?: 0,
            course = null
        )
    }

    private val _uiState = MutableStateFlow(AccountUIState(loading = true))
    val uiState = _uiState.asStateFlow()

    val guestUiState = combine(
        isGuestMode, course, guestDataFlow
    ) { isGuestMode, course, guestData ->
        if (isGuestMode) {
            guestData.copy(course = course)
        } else {
            null
        }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    init {
        viewModelScope.launch {
            if (!guestUseCase.isGuestMode()) {
                val userId = accountPrefs.getUserId()
                if (userId != null) {
                    val response = getDetails.invoke(userId)
                    if (response.isSuccess) {
                        val user = response.getOrNull()?.toUI()
                        if (user != null) {
                            _uiState.update {
                                it.copy(
                                    loading = false,
                                    name = user.username,
                                    image = if (user.photo == null) null else Uri.parse(user.photo)
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
                val isDialogVisible = _uiState.value.btnsState.showDeleteDialog
                if (isDialogVisible) {
                    viewModelScope.launch {
                        deleteAccount.invoke()
                        //  dataStoreManager.saveUserId(null)
                        _uiState.update {
                            it.copy(
                                btnsState = AccountBtnsState(showDeleteDialog = false)
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            btnsState = AccountBtnsState(showDeleteDialog = true)
                        )
                    }
                }
            }

            AccountIntent.EditClicked -> {
                //navigate to edit profile
            }

            //show warning dialog before logout
            is AccountIntent.Logout -> {
                val isDialogVisible = _uiState.value.btnsState.showLogoutDialog
                if (isDialogVisible) {
                    viewModelScope.launch {
                        logout.invoke()
                        _uiState.update {
                            it.copy(btnsState = AccountBtnsState(showLogoutDialog = false))
                        }
                        intent.goToAuth()
                    }
                } else {
                    _uiState.update {
                        it.copy(btnsState = AccountBtnsState(showLogoutDialog = true))
                    }
                }
            }

            AccountIntent.DismissDialog -> {
                _uiState.update {
                    it.copy(
                        btnsState = it.btnsState.copy(
                            showLogoutDialog = false,
                            showDeleteDialog = false
                        )
                    )
                }
            }

            is AccountIntent.Auth -> {
                Log.e("AccountViewModel", "Save account:\n${_uiState.value.guestData}")
                val email = _authState.value?.email ?: return
                val password = _authState.value?.password ?: return
                val course = _uiState.value.guestData?.course ?: return
                viewModelScope.launch {
                    val result = createUser.invoke(email, password, course.toData())
                    if (result.isFailure) {
                        handleError(result)
                    } else {
                        intent.goToHome()
                    }
                }
            }

            is AccountIntent.OnEmailChanged -> handleNewLogin(intent.login)
            is AccountIntent.OnPasswordChanged -> handleNewPassword(intent.password)
            AccountIntent.createAccount -> {
                _uiState.update {
                    it.copy(
                        btnsState = AccountBtnsState(
                            showAuthScreen = true
                        )
                    )
                }
                _authState.value = AuthUIState(screenState = AuthScreenState.REGISTER)
            }

            AccountIntent.onAuthClose -> {
                _uiState.update { it.copy(btnsState = AccountBtnsState()) }
                _authState.value = null
            }

            is AccountIntent.GoogleSign -> {
                handleGoogleSign(intent.firebaseUser, intent.goToHome)
            }
        }
    }

    private fun <T> handleError(response: Result<T>) {
        val error = response.exceptionOrNull()
        val errorMsg = when (error?.message) {
            "Wrong password" -> AuthError.WRONG_PASSWORD
            "User does not exist" -> AuthError.USER_DOES_NOT_EXIST
            "User already exists" -> AuthError.EMAIL_TAKEN
            "Failed to connect" -> AuthError.INTERNET_CONNECTION_ERROR
            "Check your internet connection" -> AuthError.INTERNET_CONNECTION_ERROR
            else -> AuthError.DEFAULT
        }
        _authState.update { it?.copy(error = errorMsg, isLoading = false) }
    }

    private fun handleNewLogin(login: String) {
        _authState.update { it?.copy(email = login, error = null) }
    }

    private fun handleNewPassword(password: String) {
        _authState.update {
            it?.copy(password = password, error = null)
        }
    }

    private fun handleGoogleSign(
        firebaseUser: FirebaseUser?,
        goToHome: () -> Unit
    ) {
        if (firebaseUser == null) {
            Log.e("handleGoogleSign", "user is null, need to handle error")
            return
        }
        viewModelScope.launch {
            val response = registerByFirebase.invoke(firebaseUser.toData())
            if (!response.isSuccess) {
                handleError(response)
                return@launch
            }
            val userId = response.getOrNull()
            if (userId != null) {
                goToHome()
            } else {
                handleError(response)
            }
        }
    }

}