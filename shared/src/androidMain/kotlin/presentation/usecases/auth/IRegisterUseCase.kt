package presentation.usecases.auth

interface IRegisterUseCase {
    suspend fun invoke(
        email: String,
        username: String,
        password: String
    ): Result<String>
}