package translator.data.repository.user

import translator.data.safeCall
import translator.data.api.ApiService
import translator.data.model.UserEntity
import translator.data.model.base.Result
import java.util.UUID

class UserRepository(private val apiService: ApiService) : IUserRepository {
    override suspend fun getUserById(id: UUID): Result<UserEntity> {
        return safeCall(
            request = { apiService.getUserById(id.toString()) }
        )
    }
}