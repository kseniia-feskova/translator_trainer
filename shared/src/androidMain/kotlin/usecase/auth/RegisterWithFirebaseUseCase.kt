package usecase.auth

import data.prefs.IDataStoreManager
import data.repository.IAuthRepository
import presentation.model.FirebaseUser
import presentation.usecases.auth.IRegisterWithFirebaseUseCase

class RegisterWithFirebaseUseCase(
    private val repo: IAuthRepository,
    private val dataStore: IDataStoreManager
) : IRegisterWithFirebaseUseCase {

    override suspend fun invoke(
        user: FirebaseUser
    ): Result<String> {
        user.email?.let { dataStore.saveEmail(it) }
        val response = repo.registerWithFirebase(
            uuid = user.uuid,
            email = user.email,
            phone = user.phone,
            photo = user.photo,
            displayName = user.displayName
        )
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