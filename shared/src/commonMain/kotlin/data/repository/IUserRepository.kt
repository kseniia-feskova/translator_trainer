package data.repository

import data.model.user.UserEntity
import data.model.base.Result

interface IUserRepository {

    suspend fun getUserById(id: String): Result<UserEntity>
}
