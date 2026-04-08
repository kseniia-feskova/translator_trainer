package domain.usecases

import data.model.user.UserEntity
import data.repository.user.IUserRepository
import domain.token.ICheckToken
import domain.token.ITokenRefresher

class GetAccountUseCase(
    private val repo: IUserRepository,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken
) : IGetAccountUseCase {

    override suspend fun invoke(userId: String): Result<UserEntity> {
        val response = checkToken.safeApiCallWithRefresh(
            call = { repo.getUserById(userId) },
            onTokenExpired = { tokenRefresher.refreshToken() })
        val data = response.data
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data)

    }
}