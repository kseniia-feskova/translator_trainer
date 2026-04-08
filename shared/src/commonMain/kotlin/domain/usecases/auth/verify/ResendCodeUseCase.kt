package domain.usecases.auth.verify

import data.prefs.IDataStoreManager
import data.repository.auth.IAuthRepository

class ResendCodeUseCase(
    private val dataStore: IDataStoreManager,
    private val authRepo: IAuthRepository
) : IResendCodeUseCase {

    override suspend fun invoke(): Result<Unit> {
        val email = dataStore.getEmail() ?: return Result.failure(Exception("User does not exist"))
        authRepo.resendCode(email)
        val response = authRepo.resendCode(email)
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else {
            val error = response.data?.error
            if (error == "Verification is needed") {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Can not change verification code"))
            }
        }
    }
}