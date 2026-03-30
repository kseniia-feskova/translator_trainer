package domain.usecases.auth

import data.prefs.IDataStoreManager
import data.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow

class LoginUseCase(private val repo: IAuthRepository, private val dataStore: IDataStoreManager) :
    ILoginUseCase {

    override suspend fun invoke(email: String, username: String, password: String): Result<String> {
        val response = repo.login(email, username, password)
        return if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else {
            val userId = response.data?.uuid
            if (userId != null) {
                dataStore.saveUserId(userId)
                Result.success(userId)
            } else {
                Result.failure(Exception("User does not exist"))
            }
        }
    }

    override fun listenUserId(): Flow<String?> {
        return dataStore.listenUserId()
    }
}