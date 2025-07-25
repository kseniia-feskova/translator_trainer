package presentation.usecases

interface IAccountUseCase {

    suspend fun getUserId(): String?
}