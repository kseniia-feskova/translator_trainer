package presentation.usecases.auth

import presentation.model.FirebaseUser

interface IRegisterWithFirebaseUseCase {
    suspend fun invoke(user: FirebaseUser): Result<String>
}