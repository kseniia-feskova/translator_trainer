package presentation.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import presentation.usecases.auth.ILoginUseCase
import presentation.usecases.auth.IRegisterUseCase
import presentation.usecases.auth.ISetGuestUseCase
import presentation.usecases.course.ICoursesOnPrefsUseCases
import presentation.usecases.course.IGetAllCoursesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.model.FirebaseUser
import presentation.usecases.auth.IRegisterWithFirebaseUseCase

class AuthViewModel(
    private val login: ILoginUseCase,
    private val register: IRegisterUseCase,
    private val registerByFirebase: IRegisterWithFirebaseUseCase,
    private val getCourses: IGetAllCoursesUseCase,
    private val coursesPrefs: ICoursesOnPrefsUseCases,
    private val guestPrefs: ISetGuestUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUIState())
    val uiState = _uiState.asStateFlow()

    fun handleIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.OnEmailChanged -> handleNewLogin(intent.login)
            is AuthIntent.OnPasswordChanged -> handleNewPassword(intent.password)
            is AuthIntent.Auth -> handleAuth(
                intent.goToCourses,
                intent.goToHome,
                intent.goToVerification
            )

            is AuthIntent.SaveAsGuest -> handleGuest(intent.goToCourses)
            AuthIntent.ChangeScreen -> handleScreenChange()
            is AuthIntent.GoogleSign -> handleGoogleSign(intent.firebaseUser,intent.goToCourses, intent.goToHome)
        }
    }

    private fun handleNewLogin(login: String) {
        _uiState.update { it.copy(email = login, error = null) }
    }

    private fun handleNewPassword(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    private fun handleAuth(
        goToCourses: () -> Unit,
        goToHome: () -> Unit,
        goToVerification: () -> Unit
    ) {
        val state = _uiState.value
        if (state.fieldsValid()) {
            _uiState.update { it.copy(isLoading = true) }
            when (state.screenState) {
                AuthScreenState.LOGIN -> login(state, goToCourses, goToHome)
                AuthScreenState.REGISTER -> register(state, goToCourses, goToVerification)
            }
        } else {
            _uiState.update { it.copy(error = AuthError.EMPTY_FIELDS) }
        }
        _uiState.update { it.copy(isLoading = false) }
    }

    private fun login(
        state: AuthUIState,
        goToCourses: () -> Unit,
        goToHome: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val response = login.invoke(state.email, state.email, state.password)
            if (!response.isSuccess) {
                handleError(response)
                return@launch
            }
            val userId = response.getOrNull()
            if (userId != null) {
                checkCourses(userId, goToCourses, goToHome)
            } else {
                _uiState.update {
                    it.copy(
                        error = AuthError.USER_DOES_NOT_EXIST,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun handleGoogleSign(
        firebaseUser: FirebaseUser?,
        goToCourses: () -> Unit,
        goToHome: () -> Unit
    ) {
        if (firebaseUser == null) {
            Log.e("handleGoogleSign", "user is null, need to handle error")
            return
        }
        viewModelScope.launch {
            val response = registerByFirebase.invoke(firebaseUser)
            if (!response.isSuccess) {
                handleError(response)
                return@launch
            }
            val userId = response.getOrNull()
            if (userId != null) {
                checkCourses(userId, goToCourses, goToHome)
            } else {
                _uiState.update {
                    it.copy(
                        error = AuthError.USER_DOES_NOT_EXIST,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun checkCourses(userId: String, goToCourses: () -> Unit, goToHome: () -> Unit) {
        viewModelScope.launch {
            val response = getCourses.invoke(userId)
            if (!response.isSuccess) {
                handleError(response)
                return@launch
            }
            val courses = response.getOrNull() ?: return@launch
            if (courses.size != 1) {
                coursesPrefs.saveAll(courses)
                goToCourses()
                _uiState.update { it.copy(isLoading = false) }
            } else {
                coursesPrefs.saveOne(courses.first())
                goToHome()
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun register(
        state: AuthUIState,
        goToCourses: () -> Unit,
        goToVerification: () -> Unit
    ) {
        viewModelScope.launch {
            val response = register.invoke(state.email, state.email, state.password)
            if (!response.isSuccess) {
                if (response.exceptionOrNull()?.message == "Verification is needed") {
                    goToVerification()
                    return@launch
                }
                handleError(response)
                return@launch
            }
            val userId = response.getOrNull()
            if (userId != null) {
                _uiState.update { it.copy(isLoading = false) }
                goToCourses()
                Log.e("NewLoginVM", "Register success")
            } else {
                _uiState.update {
                    it.copy(
                        error = AuthError.USER_DOES_NOT_EXIST,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun handleGuest(goToCourses: () -> Unit) {
        viewModelScope.launch {
            guestPrefs.setGuest()
            goToCourses()
        }
    }

    private fun handleScreenChange() {
        _uiState.update {
            it.copy(
                screenState = if (it.screenState == AuthScreenState.LOGIN) AuthScreenState.REGISTER else AuthScreenState.LOGIN,
                error = null
            )
        }
    }

    private fun AuthUIState.fieldsValid() = email.isNotEmpty() && password.isNotEmpty()

    private fun <T> handleError(response: Result<T>) {
        val error = response.exceptionOrNull()
        val errorMsg = when (error?.message) {
            "Wrong password" -> AuthError.WRONG_PASSWORD
            "User does not exist" -> AuthError.USER_DOES_NOT_EXIST
            "User already exists" -> AuthError.EMAIL_TAKEN
            "Failed to connect" -> AuthError.INTERNET_CONNECTION_ERROR
            else -> AuthError.DEFAULT
        }
        _uiState.update { it.copy(error = errorMsg, isLoading = false) }
    }
}