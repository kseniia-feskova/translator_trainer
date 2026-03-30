package domain.usecases.auth.verify

import data.prefs.IDataStoreManager
import data.repository.IAuthRepository

class VerifyCodeUseCase(
    private val authRepository: IAuthRepository,
    private val dataStore: IDataStoreManager,
) : IVerifyCodeUseCase {

    override suspend fun invoke(code: String): Result<String> {
        val email = dataStore.getEmail() ?: return Result.failure(Exception("User does not exist"))
        val response = authRepository.verify(email, code)
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else {
            val userId = response.data?.uuid
            if (userId != null) {
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