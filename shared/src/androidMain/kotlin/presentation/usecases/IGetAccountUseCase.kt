package presentation.usecases

import presentation.model.UserUI

interface IGetAccountUseCase {
    suspend fun invoke(userId: String): Result<UserUI>
}