package domain

import domain.usecases.auth.ILoginUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginUseCaseWrapper(
    private val useCase: ILoginUseCase
) {
    fun login(
        email: String,
        username: String,
        password: String,
        callback: (String?, Throwable?) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = useCase.invoke(email, username, password)
                if (result.isSuccess) {
                    callback(result.getOrNull(), null)
                } else {
                    callback(null, result.exceptionOrNull())
                }
            } catch (e: Exception) {
                callback(null, e)
            }
        }
    }
}