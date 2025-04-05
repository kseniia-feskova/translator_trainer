package com.domain.usecase.auth.verify

import com.data.prefs.IDataStoreManager
import com.data.repository.auth.IAuthRepository
import com.presentation.usecases.auth.verify.IDeleteCodeUseCase

class DeleteCodeUseCase(
    private val dataStore: IDataStoreManager,
    private val authRepo: IAuthRepository
) : IDeleteCodeUseCase {
    override suspend fun invoke() {
        val email = dataStore.getEmail() ?: return
        authRepo.clearCode(email)
    }
}