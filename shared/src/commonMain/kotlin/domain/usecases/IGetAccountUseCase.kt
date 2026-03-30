package domain.usecases

import data.model.user.UserEntity

interface IGetAccountUseCase {
    suspend fun invoke(userId: String): Result<UserEntity>
}