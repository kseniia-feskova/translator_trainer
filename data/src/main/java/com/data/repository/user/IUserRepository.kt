package com.data.repository.user

import com.data.model.UserEntity
import com.data.model.base.Result
import java.util.UUID

interface IUserRepository {

    suspend fun getUserById(id: UUID): Result<UserEntity>
}
