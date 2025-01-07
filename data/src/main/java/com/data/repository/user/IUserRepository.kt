package com.data.repository.user

import com.data.api.Result
import com.data.model.UserEntity
import java.util.UUID

interface IUserRepository {

    suspend fun getUserById(id: UUID): Result<UserEntity>
}