package domain.usecases.auth.verify

import data.prefs.IDataStoreManager
import data.repository.IAuthRepository

class DeleteCodeUseCase(
    private val dataStore: IDataStoreManager,
    private val authRepo: IAuthRepository
) : IDeleteCodeUseCase {
    override suspend fun invoke() {
        val email = dataStore.getEmail() ?: return
        authRepo.clearCode(email)
    }
}