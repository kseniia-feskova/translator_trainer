package data.repos

import data.api.ApiService
import data.model.UserEntity
import data.model.base.Result
import data.repository.IUserRepository
import data.safeCall

class UserRepository(private val apiService: ApiService) : IUserRepository {
    override suspend fun getUserById(id: String): Result<UserEntity> {
        return safeCall(
            request = { apiService.getUserById(id) }
        )
    }
}