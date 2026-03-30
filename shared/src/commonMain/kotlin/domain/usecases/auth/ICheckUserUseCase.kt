package domain.usecases.auth

import data.model.user.UserResult

interface ICheckUserUseCase {

    suspend fun invoke(userId: String): UserResult

}