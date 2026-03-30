package domain.usecases.auth

import data.model.auth.FirebaseAuthRequest
import data.prefs.IDataStoreManager
import data.repository.IAuthRepository

class RegisterWithFirebaseUseCase(
    private val repo: IAuthRepository,
    private val dataStore: IDataStoreManager
) : IRegisterWithFirebaseUseCase {

    override suspend fun invoke(
        request: FirebaseAuthRequest
    ): Result<String> {
        request.email?.let { dataStore.saveEmail(it) }
        val response = repo.registerWithFirebase(request)
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