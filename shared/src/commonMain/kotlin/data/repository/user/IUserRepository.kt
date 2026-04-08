package data.repository.user

import data.model.base.Result
import data.model.user.UserEntity

interface IUserRepository {

    suspend fun getUserById(id: String): Result<UserEntity>
}