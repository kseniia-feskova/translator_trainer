package presentation.usecases.auth

import presentation.model.UserResult

interface ICheckUserUseCase {

    suspend fun invoke(userId: String): UserResult

}