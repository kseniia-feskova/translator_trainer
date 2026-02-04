package presentation.usecases.auth

import kotlinx.coroutines.flow.Flow

interface ISetGuestUseCase {

    suspend fun setGuest()

    suspend fun resetGuest()

    suspend fun isGuestMode(): Boolean
    fun isGuestModeFlow(): Flow<Boolean>

}