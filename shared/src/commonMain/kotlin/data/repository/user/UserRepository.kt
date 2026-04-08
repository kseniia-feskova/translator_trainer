package data.repository.user

import data.api.ApiService
import data.model.base.Result
import data.model.user.UserEntity
import data.repository.safeCall

class UserRepository(private val apiService: ApiService) : IUserRepository {
    override suspend fun getUserById(id: String): Result<UserEntity> {
        return safeCall(
            request = { apiService.getUserById(id) }
        )
    }
}