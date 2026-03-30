package domain.usecases.auth

import data.model.auth.FirebaseAuthRequest

interface IRegisterWithFirebaseUseCase {
    suspend fun invoke(request: FirebaseAuthRequest): Result<String>
}