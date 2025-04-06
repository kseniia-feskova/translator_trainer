package translator.data.repository.user

import translator.data.model.UserEntity
import translator.data.model.base.Result
import java.util.UUID

interface IUserRepository {

    suspend fun getUserById(id: UUID): Result<UserEntity>
}
