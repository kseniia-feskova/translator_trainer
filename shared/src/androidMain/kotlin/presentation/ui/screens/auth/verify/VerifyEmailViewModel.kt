package presentation.ui.screens.auth.verify

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translatortrainer.shared.R
import domain.usecases.auth.verify.IDeleteCodeUseCase
import domain.usecases.auth.verify.IResendCodeUseCase
import domain.usecases.auth.verify.IVerifyCodeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerifyEmailViewModel(
    private val verifyCode: IVerifyCodeUseCase,
    private val resendCode: IResendCodeUseCase,
    private val deleteCode: IDeleteCodeUseCase
) : ViewModel() {

    private val _timerVisible = MutableStateFlow(true)
    val timerVisible = _timerVisible.asStateFlow()

    private val _enteredCode = MutableStateFlow("")
    val enteredCode = _enteredCode.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<VerifyError?>(null)
    val error = _error.asStateFlow()

    fun handleIntent(intent: VerifyIntent) {
        when (intent) {
            is VerifyIntent.OnCodeEntering -> _enteredCode.update { intent.newCode }
            is VerifyIntent.OnVerifyClicked -> verifyCode(intent.toCourse)
            VerifyIntent.ResendCodeClicked -> resendCode()
            is VerifyIntent.OnBackClicked -> deleteCode(intent.onBack)
            VerifyIntent.Timeout -> _timerVisible.update { false }
        }
    }

    private fun verifyCode(navigateToCourses: () -> Unit) {
        viewModelScope.launch {
            _loading.update { true }
            val result = verifyCode.invoke(_enteredCode.value)
            if (result.isSuccess) {
                val userId = result.getOrNull()
                if (userId != null) {
                    navigateToCourses()
                } else {
                    _error.update { VerifyError.DEFAULT }
                }
            } else {
                handleError(result)
            }
            _loading.update { false }
        }
    }

    private fun resendCode() {
        viewModelScope.launch {
            _loading.update { true }
            val result = resendCode.invoke()
            if (result.isSuccess) {
                _timerVisible.update { true }
            } else {
                handleError(result)
            }
            _loading.update { false }
        }
    }

    private fun deleteCode(navigateUp: () -> Unit) {
        viewModelScope.launch {
            deleteCode.invoke()
            navigateUp()
        }
    }

    private fun <T> handleError(response: Result<T>) {
        val error = response.exceptionOrNull()
        val errorMsg = when (error?.message) {
            "Wrong code" -> VerifyError.WRONG_CODE
            "Failed to connect" -> VerifyError.INTERNET_CONNECTION_ERROR
            "Check your internet connection" -> VerifyError.INTERNET_CONNECTION_ERROR
            else -> VerifyError.DEFAULT
        }
        _error.update { errorMsg }
    }


}

sealed class VerifyIntent {
    data class OnCodeEntering(val newCode: String) : VerifyIntent()
    data class OnBackClicked(val onBack: () -> Unit) : VerifyIntent()
    data class OnVerifyClicked(val toCourse: () -> Unit) : VerifyIntent()
    object ResendCodeClicked : VerifyIntent()
    object Timeout : VerifyIntent()
}

enum class VerifyError(@StringRes val msg: Int) {
    WRONG_CODE(R.string.wrong_code_error),
    INTERNET_CONNECTION_ERROR(R.string.internet_connection_error),
    DEFAULT(R.string.default_error)
}