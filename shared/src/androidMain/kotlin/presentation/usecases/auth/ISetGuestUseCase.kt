package presentation.usecases.auth

interface ISetGuestUseCase {

    suspend fun setGuest()

    suspend fun resetGuest()

    suspend fun isGuestMode(): Boolean

}