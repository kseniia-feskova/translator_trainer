package com.data.repository.user

import com.data.api.ApiService
import com.data.api.Result
import com.data.model.UserEntity
import com.data.safeCall
import java.util.UUID

class UserRepository(private val apiService: ApiService) : IUserRepository {
    override suspend fun getUserById(id: UUID): Result<UserEntity> {
        return safeCall(
            request = { apiService.getUserById(id.toString()) }
        )
    }
}