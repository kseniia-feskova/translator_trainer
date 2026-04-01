package domain.usecases.auth

import data.prefs.IDataStoreManager
import data.repository.IAuthRepository

class RegisterUseCase(
    private val repo: IAuthRepository,
    private val dataStore: IDataStoreManager
): IRegisterUseCase {

    override suspend fun invoke(
        email: String,
        username: String,
        password: String
    ): Result<String> {
        dataStore.saveEmail(email)
        val response = repo.register(email, username, password)
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else {
            val userId = response.data?.uuid
            if (userId != null) {
                dataStore.saveCourse(null)
                dataStore.resetGuestMode()
                dataStore.saveUserId(userId)
                Result.success(userId)
            } else {
                val errorMsg = response.data?.error
                Result.failure(Exception(errorMsg ?: "User does not exist"))
            }
        }
    }
}
