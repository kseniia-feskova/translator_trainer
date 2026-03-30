package domain.usecases

import kotlinx.coroutines.flow.Flow

interface IAccountUseCase {

    suspend fun getUserId(): String?
    fun getUserIdFlow(): Flow<String?>
}